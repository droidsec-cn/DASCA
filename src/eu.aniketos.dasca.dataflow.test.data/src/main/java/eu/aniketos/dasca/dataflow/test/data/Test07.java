/*
 * (C) Copyright 2010-2015 SAP SE.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package eu.aniketos.dasca.dataflow.test.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import eu.aniketos.dasca.dataflow.test.data.dummy.IO;


// Test Case 07:
//reachability from bad sink to bad source via multiple if-statements
public class Test07 {


    public void bad() {
        String userName = null;
        boolean local_true = true;
        if(local_true) {
            userName = IO.readLine();
        }

        if(!local_true) {
            userName = IO.readLineGood();
        }
        Connection conn = IO.getDBConnection();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM user WHERE name='" + userName + "';");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void good01() {
        String userName = null;
        boolean local_true = true;
        if(!local_true) {
            userName = IO.readLine();
        }

        if(local_true) {
            userName = IO.readLineGood();
        }
        Connection conn = IO.getDBConnection();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM user WHERE name='" + userName + "';");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test07 test = new Test07();
        test.good01();
        test.bad();
    }
}
