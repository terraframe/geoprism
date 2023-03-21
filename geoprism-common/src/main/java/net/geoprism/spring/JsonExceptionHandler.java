/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.spring;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.transport.conversion.json.ProblemExceptionDTOToJSON;
import com.runwaysdk.transport.conversion.json.RunwayExceptionDTOToJSON;

@ControllerAdvice
public class JsonExceptionHandler
{
  public static class ObjectErrorProblem implements ProblemDTOIF
  {
    private ObjectError error;

    public ObjectErrorProblem(ObjectError error)
    {
      this.error = error;
    }

    @Override
    public String getMessage()
    {
      return this.error.getDefaultMessage();
    }

    @Override
    public String getDeveloperMessage()
    {
      return this.error.getDefaultMessage();
    }

    @Override
    public void setDeveloperMessage(String developerMessage)
    {
    }

  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<String> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception
  {
    // If the exception is annotated with @ResponseStatus rethrow it and let the
    // framework handle it
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
      throw e;

    Throwable t = (Throwable) e;
    if (t instanceof InvocationTargetException)
    {
      t = ( (InvocationTargetException) e ).getTargetException();
    }

    if (t instanceof BindException)
    {
      BindException ex = (BindException) t;
      List<ObjectErrorProblem> problems = ex.getAllErrors().stream().map(er -> new ObjectErrorProblem(er)).collect(Collectors.toList());

      t = new ProblemExceptionDTO("Unable to complete request", problems);
    }

    if (t instanceof ProblemExceptionDTO)
    {
      ProblemExceptionDTOToJSON converter = new ProblemExceptionDTOToJSON((ProblemExceptionDTO) t);
      JSONObject json = converter.populate();

      return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
    }
    
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    
    // TODO: The login components for IDM and CGR don't support this
//    if (t instanceof InvalidLoginExceptionDTO || t instanceof InvalidLoginException)
//    {
//      httpStatus = HttpStatus.UNAUTHORIZED;
//    }

    RunwayExceptionDTOToJSON converter = new RunwayExceptionDTOToJSON(t.getClass().getName(), t.getMessage(), t.getLocalizedMessage());
    JSONObject json = converter.populate();

    return new ResponseEntity<String>(json.toString(), httpStatus);
  }
}