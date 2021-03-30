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
    private int id;
    private String name;
    private String phone;
    private String email;
    private String it;
    private Date insertionDate;

    public Customers() {
    }

    public Customers(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Customers(int id, String name, String phone, String email, String it) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.it = it;
    }
    
    public Customers(int id, String name, String phone, String email, String it, Date insertionDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.it = it;
        this.insertionDate = insertionDate;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String customerName) {
        this.name = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return id +  " | " + name;
    }
}
