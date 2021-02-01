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

import java.util.Date;

public class Quotes {

    private Integer id;
    private String descriptionQuote;
    private Date requestDate;
    private Double price;
    private String existence;
    private String realization;
    private String report;
    private Integer customerId;
    private String customerName;

    public Quotes() {
    }
    
    public Quotes(Integer id, String descriptionQuote, Date requestDate, Double price, String existence, String realization, String report, String customerName) {
        this.id = id;
        this.descriptionQuote = descriptionQuote;
        this.requestDate = requestDate;
        this.price = price;
        this.existence = existence;
        this.realization = realization;
        this.report = report;
        this.customerName = customerName;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriptionQuote() {
        return descriptionQuote;
    }

    public void setDescriptionQuote(String descriptionQuote) {
        this.descriptionQuote = descriptionQuote;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExistence() {
        return existence;
    }

    public void setExistence(String existence) {
        this.existence = existence;
    }

    public String getRealization() {
        return realization;
    }

    public void setRealization(String realization) {
        this.realization = realization;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }    
}
