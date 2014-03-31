  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccw.avg.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ccwhk
 */
public class ClosePrice {
    public final Date date;
    public final BigDecimal price;

    public ClosePrice(Date date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }
    
            
}
