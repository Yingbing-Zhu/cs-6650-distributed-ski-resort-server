import com.google.gson.Gson;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.ResponseMsg;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

public class SkierServlet extends HttpServlet {
    private Gson gson = new Gson();
    // Create a response message
    ResponseMsg responseMsg = new ResponseMsg();

    // handle a POST request
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");

        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseMsg.setMessage("no url");
            res.getWriter().write(gson.toJson(responseMsg));
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        if (!isUrlValid(urlParts)) {
            responseMsg.setMessage("invalid url");
            res.getWriter().write(gson.toJson(responseMsg));
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            // Parse path parameters
            int resortID = Integer.parseInt(urlParts[1]);
            String seasonID = urlParts[3];
            String dayID = urlParts[5];
            int skierID = Integer.parseInt(urlParts[7]);

            // Parse JSON body
            LiftRide liftRide = gson.fromJson(req.getReader(), LiftRide.class);
            if (liftRide == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            responseMsg.setMessage(String.format(
                    "Lift ride recorded successfully: Time - %d, Lift ID - %d, Resort ID - %d, Season ID - %s, Day ID - %s, Skier ID - %d",
                    liftRide.getTime(), liftRide.getLiftID(), resortID, seasonID, dayID, skierID
            ));
            res.getWriter().write(gson.toJson(responseMsg));
            res.setStatus(HttpServletResponse.SC_CREATED);
        }



    }
    // handle a GET request
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseMsg.setMessage("no url");
            res.getWriter().write(gson.toJson(responseMsg));
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        if (!isUrlValid(urlParts)) {
            responseMsg.setMessage("invalid url");
            res.getWriter().write(gson.toJson(responseMsg));
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            // Parse path parameters
            int resortID = Integer.parseInt(urlParts[1]);
            String seasonID = urlParts[3];
            String dayID = urlParts[5];
            int skierID = Integer.parseInt(urlParts[7]);

            responseMsg.setMessage(String.format(
                    "Lift ride vertical value get successfully for: Resort ID - %d, Season ID - %s, Day ID - %s, Skier ID - %d",
                    resortID, seasonID, dayID, skierID
            ));
            res.getWriter().write(gson.toJson(responseMsg));
            res.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        // urlPath  = "/1/seasons/2019/day/1/skier/123"
        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        // Check if the URL path has the exact number of parts required
        if (urlPath.length != 8) {
            return false;
        }
        if (!"seasons".equals(urlPath[2]) || !"days".equals(urlPath[4]) || !"skiers".equals(urlPath[6])) {
            return false;
        }
        // validation for dayID and check for integer type
        return isValidDayID(urlPath[5]) && isInteger(urlPath[1]) && isInteger(urlPath[7]);
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDayID(String dayID) {
        try {
            int day = Integer.parseInt(dayID);
            return day >= 1 && day <= 366;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}