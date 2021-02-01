/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.models;

import java.sql.Date;

public class Customers {
    private Integer id;
    private String customerName;
    private String customerNumber;
    private String customerEmail;
    private String it;
    private Date insertionDate;

    public Customers() {
    }

    public Customers(Integer id, String customerName) {
        this.id = id;
        this.customerName = customerName;
    }
    
    public Customers(Integer id, String customerName, String customerNumber, String customerEmail, String it) {
        this.id = id;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.customerEmail = customerEmail;
        this.it = it;
    }
    
    public Customers(Integer id, String customerName, String customerNumber, String customerEmail, String it, Date insertionDate) {
        this.id = id;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.customerEmail = customerEmail;
        this.it = it;
        this.insertionDate = insertionDate;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    @Override
    public String toString (){
        return getId()+  " | " + getCustomerName();
    }
}
