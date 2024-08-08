package com.jio.cohorts.dao.impl;

import com.jio.cohorts.dao.DemoDao;

import com.jio.cohorts.pojo.Member;
import com.jio.cohorts.pojo.WaitingListMembers;
import com.jio.cohorts.pojo.PaymentStatus;
import com.jio.cohorts.pojo.User;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DemoDaoImpl implements DemoDao {

    @Autowired
    private ComboPooledDataSource dataSource;

    public boolean checkCredentials(String username, String password) throws SQLException {
        var connection = dataSource.getConnection();
        PreparedStatement checkCredentialsStatement = null;
        ResultSet checkCredentialsRS = null;

        try {
            checkCredentialsStatement = connection.prepareStatement("SELECT username, password FROM Users WHERE username = ? AND password = ?");
            checkCredentialsStatement.setString(1, username);
            checkCredentialsStatement.setString(2, password);
            checkCredentialsRS = checkCredentialsStatement.executeQuery();

            if (checkCredentialsRS.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (checkCredentialsRS != null) {
                try {
                    checkCredentialsRS.close();
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
            if (checkCredentialsStatement != null) {
                try {
                    checkCredentialsStatement.close();
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
        }

        return false;
    }

    public List<PaymentStatus> getPaymentStatus(String username) throws Exception {
        List<PaymentStatus> paymentStatusList = new ArrayList<>();
        var connection = dataSource.getConnection();
        PreparedStatement checkPaymentStatusStatement = null;
        ResultSet checkPaymentStatusRS = null;

        try {
            checkPaymentStatusStatement = connection.prepareStatement("""
                    SELECT
                        ps.paymentstatus, ps.fk_mid
                    FROM
                        paymentstatus ps
                            JOIN
                        members m ON ps.fk_mid = m.mid
                            JOIN
                        users u ON m.fk_userid = u.userid
                    WHERE
                        u.username = ?""");
            checkPaymentStatusStatement.setString(1, username);

            checkPaymentStatusRS = checkPaymentStatusStatement.executeQuery();
            while (checkPaymentStatusRS.next()) {
                PaymentStatus paymentStatus = new PaymentStatus();
                paymentStatus.setFk_mid(checkPaymentStatusRS.getInt("fk_mid"));
                paymentStatus.setPaymentstatus(checkPaymentStatusRS.getString("paymentstatus"));
                paymentStatusList.add(paymentStatus); // Don't forget to add to the list
            }

        } catch (SQLException e) {
            throw new SQLException("Error getting payment status", e);
        } finally {
            if (checkPaymentStatusRS != null) {
                try {
                    checkPaymentStatusRS.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing ResultSet", e);
                }
            }
            if (checkPaymentStatusStatement != null) {
                try {
                    checkPaymentStatusStatement.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing Connection", e);
                }
            }
        }
        return paymentStatusList;
    }

    public void memberpaymentformation(int mid) throws SQLException {
        var connection = dataSource.getConnection();
        PreparedStatement memberPaymentConfirmationStatement = null;

        try {
            memberPaymentConfirmationStatement = connection.prepareStatement("INSERT INTO paymentstatus (fk_mid, paymentstatus)\n" +
                    "VALUES (?,?);");
            memberPaymentConfirmationStatement.setInt(1, mid);
            memberPaymentConfirmationStatement.setString(2, "paid");
            memberPaymentConfirmationStatement.executeUpdate();


        } catch (SQLException e) {
            throw new SQLException("Error adding member payment confirmation", e);

        } finally {
            if (memberPaymentConfirmationStatement != null) {
                try {
                    memberPaymentConfirmationStatement.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing Connection", e);
                }
            }
        }


    }


    public void addNewMember(Member member) throws SQLException {
        String memberFullName = member.getFullname();
        String rilemailid = member.getRilemailid();
        int contactNumber = member.getContactnumber();
        int fk_userid = member.getFk_userid();

        var connection = dataSource.getConnection();
        PreparedStatement addNewMemberStatement = null;

        try {
            addNewMemberStatement = connection.prepareStatement("INSERT INTO members (fullname, rilemailid, contactnumber,fk_userid) VALUES (?, ?, ?,?)");
            addNewMemberStatement.setString(1, memberFullName);
            addNewMemberStatement.setString(2, rilemailid);
            addNewMemberStatement.setInt(3, contactNumber);
            addNewMemberStatement.setInt(4, fk_userid);
            addNewMemberStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error adding new member", e);
        } finally {
            if (addNewMemberStatement != null) {
                try {
                    addNewMemberStatement.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing Connection", e);
                }
            }
        }
    }


    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();


        try (
                var connection = dataSource.getConnection();
                PreparedStatement getAllMembersStatement = connection.prepareStatement("SELECT * FROM members");
                ResultSet getAllMembersResultSet = getAllMembersStatement.executeQuery()
        ) {
            while (getAllMembersResultSet.next()) {
                Member member = new Member();

                member.setMemberid(getAllMembersResultSet.getInt("mid"));
                member.setFullname(getAllMembersResultSet.getString("fullname"));
                member.setRilemailid(getAllMembersResultSet.getString("rilemailid"));

                members.add(member);
            }
        }

        return members;
    }


    public void addNewWaitingListMember(WaitingListMembers waitingListMembers) throws SQLException {

        String fullName = waitingListMembers.getFullname();
        String rilemailId = waitingListMembers.getRilemailid();
        int contactno = waitingListMembers.getContactnumber();
        int fk_userid = waitingListMembers.getFk_userid();
        PreparedStatement addNewWaitingListMemberStatement = null;
        var connection = dataSource.getConnection();
        try {
            addNewWaitingListMemberStatement = connection.prepareStatement(
                    "INSERT INTO `waitinglist` " +
                            "(`fullname`, `rilemailid`, `contactnumber`, `fk_userid`) " +
                            "VALUES (?, ?, ?, ?)"
            );
            addNewWaitingListMemberStatement.setString(1, fullName);
            addNewWaitingListMemberStatement.setString(2, rilemailId);
            addNewWaitingListMemberStatement.setInt(3, contactno);
            addNewWaitingListMemberStatement.setInt(4, fk_userid);
            addNewWaitingListMemberStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding new waiting list member", e);
        } finally {
            if (addNewWaitingListMemberStatement != null) {
                try {
                    addNewWaitingListMemberStatement.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing Connection", e);
                }
            }
        }
    }

    public void MovewaitingListMember(int wid) throws SQLException {
        PreparedStatement movewaitingListMemberStatement = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;
        var connection = dataSource.getConnection();
        ResultSet movewaitingListMemberRS = null;
        try {
            movewaitingListMemberStatement = connection.prepareStatement("SELECT fullname,rilemailid,contactnumber,fk_userid FROM WaitingList WHERE wid = ?");
            movewaitingListMemberStatement.setInt(1, wid);
            movewaitingListMemberRS = movewaitingListMemberStatement.executeQuery();
            if (movewaitingListMemberRS.next()) {
                String memberName = movewaitingListMemberRS.getString("fullname");
                String rilemailid = movewaitingListMemberRS.getString("rilemailid");
                int contactnumber = movewaitingListMemberRS.getInt("contactnumber");
                int fk_userid = movewaitingListMemberRS.getInt("fk_userid");

                deleteStmt = connection.prepareStatement("DELETE FROM WaitingList WHERE wid = ?");
                deleteStmt.setInt(1, wid);
                deleteStmt.executeUpdate();


                insertStmt = connection.prepareStatement("INSERT INTO Members (fullname,rilemailid,contactnumber,fk_userid) VALUES (?,?,?,?)");
                insertStmt.setString(1, memberName);
                insertStmt.setString(2,rilemailid);
                insertStmt.setInt(3,contactnumber);
                insertStmt.setInt(4,fk_userid);

                insertStmt.executeUpdate();
            } else {
                System.out.println("No member found with wid: " + wid);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (movewaitingListMemberRS != null) movewaitingListMemberRS.close();
                if (movewaitingListMemberRS != null) movewaitingListMemberStatement.close();
                if (deleteStmt != null) deleteStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
