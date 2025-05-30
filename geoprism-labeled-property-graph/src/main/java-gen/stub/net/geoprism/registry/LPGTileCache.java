/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import net.geoprism.configuration.GeoprismProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.orientdb.OrientDBRequest;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionType;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.tile.PublisherUtil;
import net.geoprism.registry.tile.VersionVectorTileBuilder;

import jakarta.validation.constraints.NotNull;

public class LPGTileCache extends LPGTileCacheBase {
    public abstract static class TileCallable implements Callable<byte[]> {
        private final ThreadTransactionState state;

        private final String versionId;

        private final String typeCode;

        private final int x;

        private final int y;

        private final int zoom;

        public TileCallable(ThreadTransactionState state, String versionId, String typeCode, int x, int y, int zoom) {
            super();
            this.state = state;
            this.versionId = versionId;
            this.typeCode = typeCode;
            this.x = x;
            this.y = y;
            this.zoom = zoom;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZoom() {
            return zoom;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public String getVersionId() {
            return versionId;
        }

        protected abstract byte[] generateTile();

        @Override
        public byte[] call() throws Exception {
            return this.call(this.state);
        }

        @Request(RequestType.THREAD)
        public byte[] call(ThreadTransactionState state) {
            try {
                /*
                 * Re-check
                 */
                byte[] cached = getCachedTile(versionId, typeCode, x, y, zoom);

                if (cached != null) {
                    return cached;
                }

                byte[] tile = generateTile();

                this.populateTile(state, tile);

                return tile;
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }


        @Transaction(TransactionType.THREAD)
        private void populateTile(ThreadTransactionState state, byte[] tile) {
            LPGTileCache cache = new LPGTileCache();
            cache.setVersionId(this.versionId);
            cache.setTypeCode(this.typeCode);
            cache.setX(this.x);
            cache.setY(this.y);
            cache.setZ(this.zoom);
            cache.setTile(tile);
            cache.apply();
        }
    }

    private static class CacheCallable extends TileCallable implements Callable<byte[]> {
        public CacheCallable(ThreadTransactionState state, String versionId, String typeCode, int x, int y, int zoom) {
            super(state, versionId, typeCode, x, y, zoom);
        }

        protected byte[] generateTile() {
            // First ensure that orientdb is active on this thread
            OrientDBRequest request = (OrientDBRequest) GraphDBService.getInstance().getGraphDBRequest();
            ODatabaseSession oDatabaseSession = request.getODatabaseSession();

            if (!oDatabaseSession.isActiveOnCurrentThread()) {
                oDatabaseSession.activateOnCurrentThread();
            }

            Envelope envelope = PublisherUtil.getEnvelope(this.getX(), this.getY(), this.getZoom());
            Envelope bounds = PublisherUtil.getTileBounds(envelope);

            LabeledPropertyGraphTypeVersion version = LabeledPropertyGraphTypeVersion.get(this.getVersionId());

            VersionVectorTileBuilder builder = new VersionVectorTileBuilder(version, this.getTypeCode());
            return builder.writeVectorTiles(envelope, bounds);
        }
    }

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -1910869074;

    public static final ExecutorService executor = Executors.newFixedThreadPool(GeoprismProperties.getNumberOfTileThreads(), new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r, "tile-builder");
            t.setDaemon(false);
            return t;
        }
    });

    public LPGTileCache() {
        super();
    }

    public static byte[] getTile(JSONObject object) throws JSONException {
        String versionId = object.getString("oid");
        String typeCode = object.getString("typeCode");
        int x = object.getInt("x");
        int y = object.getInt("y");
        int zoom = object.getInt("z");

        return getTile(versionId, typeCode, x, y, zoom);
    }

    @Transaction
    private static byte[] getTile(String versionId, String typeCode, int x, int y, int zoom) {
        byte[] cached = getCachedTile(versionId, typeCode, x, y, zoom);

        if (cached != null) {
            return cached;
        } else {
            /*
             * Store the tile into the cache for future reads
             */
            SessionIF session = Session.getCurrentSession();

            if (session != null) {
                ThreadTransactionState state = ThreadTransactionState.getCurrentThreadTransactionState();

                try {
                    return executor.submit(new CacheCallable(state, versionId, typeCode, x, y, zoom)).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new ProgrammingErrorException(e);
                }
            }

            return null;
        }
    }

    public static byte[] getCachedTile(String versionId, String typeCode, int x, int y, int zoom) {
        LPGTileCacheQuery query = new LPGTileCacheQuery(new QueryFactory());
        query.WHERE(query.getVersion().EQ(versionId));
        query.WHERE(query.getTypeCode().EQ(typeCode));
        query.AND(query.getX().EQ(x));
        query.AND(query.getY().EQ(y));
        query.AND(query.getZ().EQ(zoom));

        try (OIterator<? extends LPGTileCache> it = query.getIterator()) {
            if (it.hasNext()) {
                LPGTileCache tile = it.next();
                return tile.getTile();
            }

            return null;
        }
    }

    public static void deleteTiles(LabeledPropertyGraphTypeVersion version) {
        LPGTileCacheQuery query = new LPGTileCacheQuery(new QueryFactory());
        query.WHERE(query.getVersion().EQ(version));

        try (OIterator<? extends LPGTileCache> it = query.getIterator()) {
            while (it.hasNext()) {
                it.next().delete();
            }
        }
    }

}
