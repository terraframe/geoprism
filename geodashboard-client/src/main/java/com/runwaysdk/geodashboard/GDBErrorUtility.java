package com.runwaysdk.geodashboard;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.generation.loader.Reloadable;

public class GDBErrorUtility implements Reloadable
{
  public static final String ERROR_MESSAGE_ARRAY = "errorMessageArray";

  public static final String ERROR_MESSAGE       = "errorMessage";

  public static final String DEVELOPER_MESSAGE   = "developerMessage";

  public static final String MESSAGE_ARRAY       = "messageArray";

  public static void prepareProblems(ProblemExceptionDTO e, HttpServletRequest req, boolean ignoreNotifications)
  {
    List<String> messages = new LinkedList<String>();

    for (ProblemDTOIF problem : e.getProblems())
    {
      if ((!ignoreNotifications) && ( problem instanceof AttributeNotificationDTO ))
      {
        String message = problem.getMessage();

        messages.add(message);
      }
    }

    if (messages.size() > 0)
    {
      req.setAttribute(ERROR_MESSAGE_ARRAY, messages.toArray(new String[messages.size()]));
    }
  }
  
  public static void prepareThrowable(Throwable t, HttpServletRequest req)
  {
    String localizedMessage = t.getLocalizedMessage();

    req.setAttribute(ERROR_MESSAGE, localizedMessage);

    if (t instanceof ProgrammingErrorExceptionDTO)
    {
        ProgrammingErrorExceptionDTO pee = (ProgrammingErrorExceptionDTO) t;
        String developerMessage = pee.getDeveloperMessage();

        req.setAttribute(DEVELOPER_MESSAGE, developerMessage);

    }
  }
  
}
