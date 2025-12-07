package com.safvan.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.safvan.beans.Ticket;
import com.safvan.beans.TicketDTO;
import com.safvan.beans.Train;
import com.safvan.beans.TrainDTO;
import com.safvan.beans.User;
import com.safvan.constants.endpoints.UserEndpoints;
import com.safvan.exception.restapi.booking.ApiBookingFailedException;
import com.safvan.exception.restapi.booking.ApiNoEnoughSeatsForBooking;
import com.safvan.exception.restapi.train.ApiTrainNotFoundException;
import com.safvan.exception.mvc.train.TrainNotFoundException;
import com.safvan.service.mvc.IBookingService;
import com.safvan.service.mvc.ILoginManagementService;
import com.safvan.service.mvc.ITrainService;
import com.safvan.util.PayUtil;
import com.alipay.api.AlipayApiException;

/**
 * UserController handles operations related to user or customer functionality.
 * This includes viewing available trains, searching for trains between
 * stations, booking tickets, viewing booking history, checking seat
 * availability, and more.
 * 
 * @author Safvan
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping(UserEndpoints.USER_BASE_URI)
public class UserController {

    @Autowired
    private ITrainService trainService;

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ILoginManagementService loginManagementService;

    @Autowired
    private PayUtil payUtil;

    /**
     * Shows the home page for the user.
     * 
     * @return The view name for the user home page.
     */
    @GetMapping(value = { "/", UserEndpoints.SHOW_HOME })
    public String showHomePage() {
        return "user/user_home";
    }

    /**
     * Displays all available trains to the user.
     * 
     * @param model The model object to pass data to the view.
     * @return The view name for displaying all trains.
     */
    @GetMapping(UserEndpoints.VIEW_ALL_TRAINS)
    public String viewAllTrainsForward(Map<String, Object> model) {

        System.out.println("UserController.viewAllTrainsForward()");

        List<Train> trainsList = trainService.getAllTrains();

        trainsList.forEach(System.out::println);

        // writing page heading and setting data to request scope
        model.put("pageHeading", "All Available running Trains - displaying to user..");
        model.put("trainsList", trainsList);

        return "user/view_trains";
    }

    /**
     * Forwards to the form for searching trains between stations or finding train
     * fare between stations based on the requested URL.
     * 
     * It determines the functionality based on the requested URL and sets the
     * appropriate form field names and button labels in the model. The form is
     * designed to be dynamic, allowing the button names to change based on
     * requirements.
     * 
     * @param model   The model object to pass data to the view.
     * @param request The HTTP request object.
     * @return The view name for the trains between stations form.
     */
    @GetMapping(value = { UserEndpoints.SHOW_FIND_TRAINS_BETWEEN_TWO_STATIONS_FORM,
            UserEndpoints.SHOW_TRAIN_FAIR_ENQUERY_FORM })
    public String findTrainsbetweenStaionsForward(Map<String, Object> model,
            HttpServletRequest request) {

        System.out.println(request.getRequestURI());

        String pageHeading;
        String submitButtonValue;

        if (request.getRequestURI().equals(servletContext.getContextPath() + "/user/findTrainsbetweenStaionsFwd")) {
            pageHeading = "Search Trains Between Stations";
            submitButtonValue = "SEARCH TRAINS";
        } else {
            pageHeading = "Train Fare Enquiry";
            submitButtonValue = "SHOW FARE";
        }

        // passing page heading and button values
        model.put("pageHeading", pageHeading);
        model.put("submitButtonValue", submitButtonValue);

        return "user/trains_btwn_stations_form";
    }

    /**
     * Retrieves the list of trains between two stations based on the provided
     * station names and pass it to view for displaying.
     * 
     * This method handles the GET request for "/findTrainsbetweenStaions" URL. It
     * calls the train service to get the list of trains between the specified
     * stations. The page heading is set to display the stations being searched, and
     * the trains list is passed to the view.
     * 
     * @param fromStation The name of the starting station.
     * @param toStation   The name of the destination station.
     * @param model       The model object to pass list of trains to the view.
     * @return The view name for displaying the trains between stations.
     */
    @GetMapping(UserEndpoints.FIND_TRAINS_BETWEEN_TWO_STATIONS_RESULT)
    public String findTrainsbetweenStaions(
            @RequestParam String fromStation,
            @RequestParam String toStation,
            Map<String, Object> model) {

        // Retrieve the list of trains between the specified stations
        List<Train> trainsList = trainService.getTrainsBetweenStations(fromStation, toStation);
        trainsList.forEach(System.out::println);

        // Set the page heading and trains list in the model to pass to view
        model.put("pageHeading",
                "Trains Between stations.." + fromStation.toUpperCase() + " & " + toStation.toUpperCase());
        model.put("trainsList", trainsList);

        return "user/view_trains";
    }

    /**
     * Displays the pre-booking form for a specific train.
     *
     * this handler method retrieves the train details selected entered by user
     * through form , This train details are stored in a TrainDTO object and added
     * to the model. This user entered details are then used in view to display
     * train_pre_booking_form.
     *
     * @param trainNo     The train number for which the user want to book and this
     *                    will be diplayed in train_pre_booking_form.
     * @param fromStation The name of the from station.
     * @param toStation   The name of the to station.
     * @param session     The HttpSession object.
     * @param model       The model object to pass data to the view.
     * @return The view name for the pre-booking form.
     */
    @GetMapping(UserEndpoints.SHOW_TRAIN_PRE_BOOKING_FORM)
    public String showPreBookingFormForTrain(
            @RequestParam Long trainNo,
            @RequestParam String fromStation,
            @RequestParam String toStation,
            HttpSession session,
            Map<String, Object> model) {

        System.out.println("UserController.showPreBookingFormForTrain()+ " + trainNo);

        // Create a TrainDTO object and set the train number,
        // from station, and to station
        TrainDTO trainDTO = new TrainDTO();
        trainDTO.setTrainNo(trainNo);
        trainDTO.setFromStation(fromStation);
        trainDTO.setToStation(toStation);

        // get the user details to display in prebookig form
        String sessionId = session.getAttribute("sessionId").toString();
        User user = loginManagementService.getUserbySessionId(sessionId);

        // Store the TrainDTO object and user details in the model
        model.put("preBookingDetails", trainDTO);
        model.put("user", user);

        return "user/train_pre_booking_form";
    }

    /**
     * Proceeds with the train booking for the user.
     *
     * It receives the TicketDTO object containing the booking details trainNo,from
     * and to station ,seatType,berthPreference etc... as request parameters. A
     * TrainDTO object is created and populated with the train number, from station,
     * and to station. The TicketDTO and TrainDTO objects are added to the model.
     * The view "user/payment_inputs_form" is returned to display the payment inputs
     * form.
     *
     * @param ticketDTO   The TicketDTO object containing the booking details
     *                    including seatType,berthPreference etc...
     * @param trainNo     The train number.
     * @param fromStation The from station.
     * @param toStation   The to station.
     * @param model       The model object to pass data to the view.
     * @return The view name for the payment inputs form.
     */
    @PostMapping(UserEndpoints.PROCEED_TRAIN_BOOKING)
    public String proceedTrainBookingForUser(
            @ModelAttribute TicketDTO ticketDTO,
            @RequestParam Long trainNo,
            @RequestParam String fromStation,
            @RequestParam String toStation,
            Map<String, Object> model) {

        // Create a TrainDTO object and set the train number,
        // from station, and to station
        TrainDTO trainDTO = new TrainDTO();
        trainDTO.setTrainNo(trainNo);
        trainDTO.setFromStation(fromStation);
        trainDTO.setToStation(toStation);

        // Get complete train details from database including fare
        try {
            Train train = trainService.getTrainByNumber(trainNo);
            if (train != null) {
                trainDTO.setFare(train.getFare());

                // Calculate total amount and set it to ticketDTO
                if (train.getFare() != null && ticketDTO.getSeatsRequired() != null) {
                    Double totalAmount = train.getFare() * ticketDTO.getSeatsRequired();
                    ticketDTO.setTicketAmount(totalAmount);
                }
            }
        } catch (TrainNotFoundException e) {
            // Handle exception if needed
            e.printStackTrace();
        }

        model.put("ticketDTO", ticketDTO);
        model.put("trainDTO", trainDTO);

        return "user/payment_inputs_form";
    }

    /**
     * Confirms the train booking for the user and redirects to Alipay payment page.
     * 
     * This method receives the TrainDTO object containing the train details and the
     * TicketDTO object containing the booking details. It then prepares the payment
     * request and redirects the user to Alipay's payment page.
     * 
     * @param trainDTO  The TrainDTO object containing the train details.
     * @param ticketDTO The TicketDTO object containing the booking details.
     * @param session   the HttpSession Object.
     * @param model     The model object to store payment parameters.
     * @return The Alipay payment page HTML response.
     * @throws AlipayApiException If there's an error with Alipay API.
     */
    @PostMapping(UserEndpoints.CONFIRM_TRAIN_BOOKING)
    public String confirmTrainBooking(
            @ModelAttribute("trainDTO") TrainDTO trainDTO,
            @ModelAttribute("ticketDTO") TicketDTO ticketDTO,
            HttpSession session,
            Map<String, Object> model) throws AlipayApiException {

        // Store booking details in session for later use in callback
        session.setAttribute("trainDTO", trainDTO);
        session.setAttribute("ticketDTO", ticketDTO);

        // Prepare payment parameters
        String outTradeNo = "TRADE_" + System.currentTimeMillis(); // Generate unique trade number

        Float totalAmount = ticketDTO.getTicketAmount().floatValue(); // Get fare from ticketDTO
        String subject = "火车票预订 - " + trainDTO.getTrainNo() + "次列车";

        // Store trade number in session
        session.setAttribute("outTradeNo", outTradeNo);

        // Call Alipay API to get payment page
        String alipayForm = payUtil.sendRequestToAlipay(outTradeNo, totalAmount.floatValue(), subject);

        // Debug: Print the generated Alipay form to check method parameter
        System.out.println("Generated Alipay Form: ");
        System.out.println(alipayForm);

        // Return Alipay payment page
        model.put("alipayForm", alipayForm);
        return "user/alipay_payment_redirect";
    }

    /**
     * Handles the payment success callback from Alipay.
     * 
     * This method is called when the user successfully completes payment on Alipay.
     * It retrieves the booking details from session, completes the booking process,
     * and displays the booking result.
     * 
     * @param session The HttpSession object containing stored booking details.
     * @param model   The model object to pass ticketBookingResult to the view.
     * @return The view name for the ticket booking result.
     */
    @GetMapping("/paymentSuccess")
    public String paymentSuccess(HttpSession session, Map<String, Object> model) {
        // This is the synchronous callback from Alipay
        // The actual booking logic is the same as the asynchronous callback
        return processPaymentSuccess(session, model);
    }

    /**
     * Handles the asynchronous payment callback from Alipay server.
     * 
     * This method is called directly by Alipay server to notify the payment result.
     * It should be exposed to the internet for Alipay server to access.
     * 
     * @param request The HttpServletRequest containing payment result parameters.
     * @return A response string "success" to acknowledge receipt of the
     *         notification.
     */
    @PostMapping("/alipay/notify")
    public void alipayNotify(HttpServletRequest request, HttpSession session) {
        // Get payment result parameters from Alipay
        Map<String, String[]> params = request.getParameterMap();

        // Here you should verify the signature of the parameters to ensure they are
        // from Alipay
        // For simplicity, we'll skip signature verification in this example

        // Check if payment was successful
        String tradeStatus = params.get("trade_status")[0];

        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            // Get trade number
            String outTradeNo = params.get("out_trade_no")[0];

            // You can store this trade number in session or database to associate with
            // booking
            // For this example, we'll just log it
            System.out.println("Payment successful for trade: " + outTradeNo);

            // You might want to implement additional logic here, such as:
            // 1. Updating payment status in database
            // 2. Sending confirmation emails
            // 3. Handling any post-payment processing
        }
    }

    /**
     * Common method to process payment success.
     * 
     * @param session The HttpSession object containing stored booking details.
     * @param model   The model object to pass ticketBookingResult to the view.
     * @return The view name for the ticket booking result.
     */
    private String processPaymentSuccess(HttpSession session, Map<String, Object> model) {
        // Retrieve booking details from session
        TrainDTO trainDTO = (TrainDTO) session.getAttribute("trainDTO");
        TicketDTO ticketDTO = (TicketDTO) session.getAttribute("ticketDTO");
        String outTradeNo = (String) session.getAttribute("outTradeNo");

        // Create new Train and Ticket objects and copy property values from TrainDTO
        // and TicketDTO
        Train train = new Train();
        BeanUtils.copyProperties(trainDTO, train);

        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketDTO, ticket);

        // Set train details in the ticket object
        ticket.setTrain(train);

        // Retrieve user details based on sessionId
        String sessionId = session.getAttribute("sessionId").toString();
        User user = loginManagementService.getUserbySessionId(sessionId);

        // Add user details and transaction ID to the ticket
        ticket.setUser(user);
        ticket.setTransactionId(outTradeNo);

        // Book the ticket using the bookingService
        Ticket ticketBookingResult = bookingService.bookTicket(ticket);

        // Pass the booking results to view
        model.put("ticketBookingResult", ticketBookingResult);

        return "user/ticket_booking_result";
    }

    /**
     * Retrieves and displays the ticket booking history for the currently logged-in
     * user.
     * 
     * @param session the HttpSession object to retrieve the user's session ID.
     * @param model   the model object to pass data to the view.
     * @return the view name for displaying the ticket booking history.
     */
    @GetMapping(UserEndpoints.SHOW_TICKET_BOOKING_HISTORY)
    public String getAllTicketsBooked(
            HttpSession session,
            Map<String, Object> model) {

        // Retrieve user details of logged in user using sessionId.
        String sessionId = session.getAttribute("sessionId").toString();
        User user = loginManagementService.getUserbySessionId(sessionId);

        // Retrieve the list of tickets booked by the user
        List<Ticket> ticketsList = bookingService.getTicketsByUser(user);

        model.put("pageHeading", "Ticket Booking History");
        model.put("ticketsList", ticketsList);

        return "user/view_all_tickets";
    }

    /**
     * Shows the train number input form for checking seat availability or searching
     * a train by number.
     * 
     * Depending on the requested URL, it sets the appropriate page heading and
     * submit button value in the model for rendering the form.
     * 
     * @param request The HttpServletRequest object containing the request
     *                information.
     * 
     * @param model   The model object to pass page heading and button values to the
     *                view for dynamic form generation.
     * 
     * @return The view name for the train number input form.
     */
    @GetMapping(value = {
            UserEndpoints.SHOW_TRAIN_SEATS_AVAILABILITY_CHECK_FORM,
            UserEndpoints.SHOW_SEARCH_TRAIN_BY_NUMBER_FORM })
    public String showTrainNumberinputForm(
            HttpServletRequest request,
            Map<String, Object> model) {

        String pageHeading;
        String submitButtonValue;

        if (request.getRequestURI().equals("/user/trainSeatsAvailablityCheckFwd")) {
            pageHeading = "Train Seats Availability Check !";
            submitButtonValue = "CHECK SEATS AVAILABLE";
        } else {
            pageHeading = "Search Trains!";
            submitButtonValue = "SEARCH TRAIN";
        }

        // Set the page heading and trains list in the model to pass to view
        model.put("pageHeading", pageHeading);
        model.put("submitButtonValue", submitButtonValue);

        return "user/train_number_input_form";
    }

    /**
     * Searches for a train by its number and displays its details.
     *
     * It retrieves the train information based on the provided train number using
     * the trainService.
     *
     * @param trainNo The train number to search for.
     * @param model   The model object to pass data to the view.
     * @return The view name for displaying the train details.
     * @throws ApiTrainNotFoundException If the train is not found.
     */
    @GetMapping(UserEndpoints.SEARCH_TRAIN_BY_NUMBER_RESULT)
    public String searchTrainByNumber(
            @RequestParam Long trainNo,
            Map<String, Object> model) {

        // retrive train details based on train number
        Train train = trainService.getTrainByNumber(trainNo);

        model.put("train", train);

        return "user/display_train_details";
    }

}
