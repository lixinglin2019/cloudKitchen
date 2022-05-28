package com.cloudkitchens.orderdelivery.domain;


public class Kitchen {
    public static Kitchen kitchen ;
    private int id;
    private String code;
    private String name;
    private String address;

   private Kitchen(int id, String code, String name, String address) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
    }
    //DCL
    public static Kitchen getInstance(){
       if (kitchen==null){
           synchronized (kitchen){
               if (kitchen == null) {
                   kitchen = new Kitchen(1,"Kitchen_CODE","kitchen_name","kitchen_address");
               }
           }
       }
       return kitchen;
    }
}
