package server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that handles display of user reviews, and lets user edit and delete there reviews.
 */
public class UserReviewsServlet extends LoginBaseServlet {

    /**
     * A method that gets executed when the get request is sent to the UserReviewsServlet
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (getUsername(request) != null) {
            if(request.getParameter("username")==null){
                log.debug("Missing username in request");
                response.sendRedirect("myreviews?username="+getUsername(request));
            }else{
                prepareResponse("My reviews", response);
                printForm(request,response);
                finishResponse(response);
            }
        }
        else {
            response.sendRedirect("/login");
        }
    }
    /** The method that will handle post request sent to UserReviewsServlet.
     * @param request
     * @param response
     * */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username=request.getParameter("username");
        if(request.getParameter("delete")!=null){
            prepareResponse("Delete hotel review", response);
            Status status = databaseHandler.removeReview(username,request.getParameter("hotelid"));

            if(status == Status.OK) {
                log.debug("Review successfully removed");
                String url = "/myreviews?username="+username+"&delete=true";
                response.sendRedirect(response.encodeRedirectURL(url));
            }
            else {
                log.debug("Not able to remove review:" + status);
                String url = "/myreviews?username="+username+"&delete=true";
                url = response.encodeRedirectURL(url);
                response.sendRedirect(url);
            }
        } else if(request.getParameter("edit")!=null){
            String url = "/editreview?username="+username+"&hotelid="+request.getParameter("hotelid");
            response.sendRedirect(response.encodeRedirectURL(url));
        }

    }


    /**
     * Method that prints hotel reviews to a specific hotel. Also handles if get request is missing parameters
     * @param request
     * @param response
     */
    private void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        if(request.getParameter("username")!=null){
            String username = request.getParameter("username");
            //Check that request username is the same as the currant user.
            if(username.equalsIgnoreCase(getUsername(request))){
                //Check if currant user has any reviews
                if(databaseHandler.checkUsernameReviewSet(getUsername(request))==Status.OK){
                    out.println("<h3>Reviews for by user: "+username+" </h3>");
                    out.println(databaseHandler.usernameReviewDisplayer(username));
                }else {
                    out.println("<p>You don't have any reviews yet</p>");
                }
            }else{
                out.println("<p>Username is invalid</p>");
            }
        }else{
            out.println("Invalid request");
        }
    }
}
