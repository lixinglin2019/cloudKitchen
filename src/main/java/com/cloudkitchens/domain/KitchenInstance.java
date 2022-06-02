package com.cloudkitchens.domain;


public class KitchenInstance {
    public static KitchenInstance kitchen ;
    private int id;
    private String code;
    private String name;
    private String address;

   private KitchenInstance(int id, String code, String name, String address) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
    }
    //单例模式 DCL
    public static KitchenInstance getInstance(){
       if (kitchen==null){
           synchronized (KitchenInstance.class){
               if (kitchen == null) {
                   kitchen = new KitchenInstance(1,"Kitchen_CODE","kitchen_name","kitchen_address");
               }
           }
       }
       return kitchen;
    }
}
