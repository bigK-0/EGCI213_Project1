/*
    Panachai Kongja 6480068
    Pavorn Thongyoo 6480138
*/
package Project1_6480068;

import java.util.*;
import java.io.*;

public class Main {
    
    public static class Product {
        final String name;
        final int price,weight;
        protected int totSale = 0,unitSold = 0;
        
        public Product(String n,int p,int w) {
            name = n;
            price = p;
            weight = w;
        }
        public void addSale(int n) {totSale+=n;}
        public void addUnit(int n) {unitSold+=n;}
        public int getUS() {return unitSold;}
        public int getTS() {return totSale;}
        public String getName() {return name;}
        public int getWeight() {return weight;}
    }
    
    public static class Customer {
        protected String name;
        protected int cashback = 0;
        public Customer(String n) {name = n;}
        public void setCashback(int n) {cashback =n;} 
        public String getName() {return name;}
        public int getCashback() {return cashback;}
    }
    
    public static class Order {
        protected String name;
        protected int id;
        protected String shippingType = "S";
        protected int totWeight = 0;
        protected int totPrice = 0;
        protected int prod1;
        protected int prod2;
        protected int prod3;
        protected int prod4;
        protected int prod5;
        public Order(String n,int i,String s, int a, int b, int c, int d, int e) {
            name = n;
            id = i;
            shippingType=s;
            prod1 = a;
            prod2 = b;
            prod3 = c;
            prod4 = d;
            prod5 = e;
        }
        public void addWeight(int n) {totWeight += n;} //calculate total weight
        public void addPrice(int n) {totPrice += n;} //calculate total price
        public int getPrice() {return totPrice;}
        public int getWeight() {return totWeight;}
        public void process(Customer b, Product[] products, ShippingCalculator calc)
        {
            totPrice = (prod1*products[0].price) + (prod2*products[1].price) + 
                       (prod3*products[2].price) + (prod4*products[3].price) + 
                       (prod5*products[4].price);
            
            totWeight = (prod1*products[0].weight) + (prod2*products[1].weight) + 
                        (prod3*products[2].weight) + (prod4*products[3].weight) + 
                        (prod5*products[4].weight);
            
            int shipping = calc.calculate(getWeight(), shippingType), cashback = 0;
            if(b.getName().contains(name)) {
                cashback = b.getCashback();
                if(cashback>100){
                    b.setCashback(cashback-100);
                    cashback=100;
                }
            }
            System.out.printf("Order %d (%s) ,  %s >> %15s %s %15s %s %15s %s %15s %s %15s %s\n",
                        id,
                        shippingType,
                        name,
                        products[0].getName(),
                        prod1,
                        products[1].getName(),
                        prod2,
                        products[2].getName(),    
                        prod3,
                        products[3].getName(),
                        prod4,
                        products[4].getName(),
                        prod5
                        );
            String blank = " ".repeat(26);
                System.out.printf("%26s>> Available Cashback = %d\n",blank,cashback);
                System.out.printf("%26s>> Total Price  = %,-9d\n",blank,getPrice());
                System.out.printf("%26s>> Total Weight = %,-9d  Shipping Fee = %,d\n",blank,getWeight(),shipping);
                System.out.printf("%26s>> Final Bill   = %,-9d  Cashback for next order = %d\n\n",blank,getPrice()+shipping-cashback, (int)(getPrice() * 0.01));
                //set next order's cashback
                b.setCashback((int)(getPrice() * 0.01));
                
            // summing up total sales and total units of each product
            products[0].addSale(prod1*products[0].price);
            products[1].addSale(prod2*products[0].price);
            products[2].addSale(prod3*products[0].price);
            products[3].addSale(prod4*products[0].price);
            products[4].addSale(prod5*products[0].price);
            
            products[0].addUnit((prod1*products[0].weight)/products[0].getWeight());
            products[1].addUnit((prod2*products[1].weight)/products[1].getWeight());
            products[2].addUnit((prod3*products[2].weight)/products[2].getWeight());
            products[3].addUnit((prod4*products[3].weight)/products[3].getWeight());
            products[4].addUnit((prod5*products[4].weight)/products[4].getWeight());
            
        }
    }
    
    public static class ShippingCalculator {
        protected int eMax1, eMax2, eMax3;
        protected int eFee1, eFee2, eFee3;
        protected int sMax, sFee;
        protected int vFee,vMax;
        public ShippingCalculator() {}
        public void ef1(int n1,int n2) {
            eMax1 = n1;
            eFee1 = n2;
        }
        public void ef2(int n1,int n2) {
            eMax2 = n1;
            eFee2 = n2;
        }
        public void ef3(int n1,int n2) {
            eMax3 = n1;
            eFee3 = n2;
        }
        public void sf(int n1,int n2) {
            sMax = n1;
            sFee = n2;
        }
        public void sv(int n1,int n2) {
            vMax = n1;
            vFee = n2;
        }
        public int calculate(int tw,String shipType) {
            int shipPrice = 0;
            if(tw>eMax3 || shipType.compareTo("S")==0) {
                if(tw<sMax) return shipPrice = sFee;
                else {
                    tw-=sMax;
                    shipPrice+=sFee+Math.ceil((double)tw/vMax)*vFee;
                    return shipPrice;
                }
            } else {
                if(tw<eMax1) return eFee1;
                else if(tw<eMax2) return eFee2;
                else return eFee3;
            }
        }
    }
    
    public static void main(String[] args)
    {
        String path = "src/main/java/Project1_6480068/";
        String pdinfile = path + "products.txt";
        Scanner scanner = new Scanner(System.in);
        File file;
        Scanner rd = null;
        
        // handling missing product file
        while (rd == null) {
        file = new File(pdinfile);

            try {
              rd = new Scanner(file);
            } 
            catch (FileNotFoundException e) {
              System.out.println(e);
              System.out.println("Please enter the correct file name for products: ");
              pdinfile = path + scanner.nextLine();
            }
        }
        
        // reading product and putting into array
        Product[] products = new Product[5];
        int i = 0;
            while(rd.hasNext()) {
                String[] buf = rd.nextLine().split(",");
                products[i] = new Product(buf[0].trim(),
                                          Integer.parseInt(buf[1].trim()),
                                          Integer.parseInt(buf[2].trim())
                );
                i++;
            }
        rd.close();
        
        // handling shipping file
        String shinfile = path + "shipping.txt";
        Scanner rd2 = null;
        
        while (rd2 == null) {
        file = new File(shinfile);

            try {
              rd2 = new Scanner(file);
            } 
            catch (FileNotFoundException e) {
              System.out.println(e);
              System.out.println("Please enter the correct file name for shipping: ");
              shinfile = path + scanner.nextLine();
            }
        }
        
        // reading shipping file 
         ShippingCalculator calc = new ShippingCalculator();
            for(i=0;i<5;i++){ 
                String[] buf = rd2.nextLine().split(",");
                int var1=Integer.parseInt(buf[3].trim()), var2 = Integer.parseInt(buf[4].trim());
                switch(i) {
                    case 0:calc.ef1(var1,var2);break;
                    case 1:calc.ef2(var1,var2);break;
                    case 2:calc.ef3(var1,var2);break;
                    case 3:calc.sf(var1,var2);break;
                    case 4:calc.sv(var1,var2);break;
                }
            }
        rd2.close();
        
        // handling orders file
        String ordfile = path + "orders.txt";
        Scanner rd3 = null;
        while (rd3 == null) {
        file = new File(ordfile);

            try {
              rd3 = new Scanner(file);
            } 
            catch (FileNotFoundException e) {
              System.out.println(e);
              System.out.println("Please enter the correct file name for orders: ");
              ordfile = path + scanner.nextLine();
            }
        }
        
        // reading and handling orders file
        List<Order> orders = new ArrayList<Order>(); //USE ARRAYLIST
        List<Customer> customers = new ArrayList<Customer>();

        String name, shtype;
        
        int id, prod1, prod2, prod3, prod4, prod5;

        while (rd3.hasNext())
        {
            String line = rd3.nextLine();
            
            try
            {
                String[] buf = line.split(",");
                name = buf[1].trim();
                id = Integer.parseInt(buf[0].trim());
                shtype = buf[2].trim();
                prod1 = Integer.parseInt(buf[3].trim());
                prod2 = Integer.parseInt(buf[4].trim());
                prod3 = Integer.parseInt(buf[5].trim());
                prod4 = Integer.parseInt(buf[6].trim());
                prod5 = Integer.parseInt(buf[7].trim());

                orders.add(new Order(name,id,shtype,prod1,prod2,prod3,prod4,prod5));
                customers.add(new Customer(name));
                
            }
            catch (RuntimeException e)
            {
                System.out.println(e);
                System.out.println(line + " --> Skip this order.");
                System.out.println();
            }
            
        }
        rd3.close();
        
        // processing and printing 
        System.out.println("===== Order Processing =====");
        for(int k=0; k<orders.size();k++)
        {
            orders.get(k).process(customers.get(k), products, calc);
        }
        
        // Sorting products by total price
         for (int k=0; k<5; k++)
        {
            for (int m=k+1; m<5; m++)
            {
                if (products[k].getTS() < products[m].getTS()) 
                {
                    Product temp = products[k];
                    products[k] = products[m];
                    products[m] = temp;
                }
            }
        }
        
        System.out.println("===== Product Summary =====");        
        for(int n=0; n<5; n++)
        {
            System.out.printf("%-22s total sales = %-,10d baht, %-5d units\n", products[n].getName(), products[n].getTS(), products[n].getUS());
        }
        System.out.println();
        
    }
    
}
