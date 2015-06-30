#!/usr/bin/env python
#
# Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
#
# This file is part of Runway SDK(tm).
#
# Runway SDK(tm) is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# Runway SDK(tm) is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
#

# vim: sts=4 sw=4 et

import os, sys

printed = set()

def includes(f):
    d = os.path.dirname(f)
    for l in open(f):
        if l.startswith('//#include'):
            yield os.path.join(d, l.strip().split(None, 1)[1].strip(""""'"""))

work = list(sys.argv[1:])

while work:
    f = work.pop(0)
    if f in printed:
        continue
    i = list(filter(lambda x: x not in printed, includes(f)))
    if i:
        work = i + [f] + work
        continue
    printed.add(f)
    print f

