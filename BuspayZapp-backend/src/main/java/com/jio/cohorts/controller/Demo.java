package com.jio.cohorts.controller;

import com.google.gson.Gson;
import com.jio.cohorts.Utils.Commons;
import com.jio.cohorts.dao.DemoDao;
import com.jio.cohorts.dao.impl.DemoDaoImpl;

import com.jio.cohorts.pojo.Member;
import com.jio.cohorts.pojo.PaymentStatus;
import com.jio.cohorts.pojo.User;
import com.jio.cohorts.pojo.WaitingListMembers;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class Demo {

    @Autowired
    DemoDao demoDao;






    @Autowired
    DemoDaoImpl demo;



    @GetMapping("/login")
    public String Login(@RequestBody User user) throws SQLException {

        String username = user.getUsername();
        String password = user.getPassword();

        Boolean ifValidCredentials = demo.checkCredentials(username,password);

        if(ifValidCredentials){
            return "Valid Login Credentials";
        }
        else{
            return "Invalid Login Credentials";

        }



    }

    @GetMapping("/getallmembers")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members;
        try {
            members = demo.getAllMembers();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/getallwaitinglistmembers")
    public ResponseEntity<List<Member>> getAllWaitingListMembers() {
        List<Member> members;
        try {
            members = demo.getAllMembers();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    @GetMapping("/getpaymentstatus")
    public ResponseEntity<List<PaymentStatus>> getpaymentstatus(@RequestParam String username, HttpServletRequest request){
        List<PaymentStatus> paymentStatus;
        try{
            paymentStatus = demo.getPaymentStatus(username);



        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(paymentStatus,HttpStatus.OK);


    }

    @PostMapping("/memberpaymentconfirmation")
    public ResponseEntity<String> paymentconfirmation(@RequestParam int mid){
        try{
            demo.memberpaymentformation(mid);


            }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


            }
        return new ResponseEntity<>("Member added Succesfully",HttpStatus.OK);

    }











    @PostMapping("/addnewmember")
    public ResponseEntity<String> addnewmember(@RequestBody Member member){

        try{
            demo.addNewMember(member);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>("Added Successfully", HttpStatus.OK);






    }



    @PostMapping("/addnewwaitingListmember")
    public ResponseEntity<String> addnewwaitingListmember(@RequestBody WaitingListMembers waitingListMembers){

        try{
            demo.addNewWaitingListMember(waitingListMembers);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);


        }
        return new ResponseEntity<>("New  waiting List member added succesfully",HttpStatus.OK);
    }

    @PostMapping("/movewaitinglistmember")
    public ResponseEntity<String> moveMemberfromWaitingList(@RequestParam int wid){
        try{
            demo.MovewaitingListMember(wid);

        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);



        }
        return new ResponseEntity<>("waiting List member added  to Permanent member succesfully",HttpStatus.OK);

        

    }






}
