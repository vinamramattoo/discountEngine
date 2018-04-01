The server runs on port 9091
API address is : localhost:9091/engine/discount

The project is on spring boot running on tomcat server build on gradle
to run use
$ gradle bootRun

No database is required

29 test cases are included written in junit with Mockito

input takes json of format
{
 "outletId" = 1,
 "couponCode" = "BOGO",
 "cart_items" =[
                 {
                 "product_id" = "1",
                 "quantity" = 1 ,
                 "unit_cost" = 100 
                 },

                 {
                 "product_id" = "2",
                 "quantity" = 2 ,
                 "unit_cost" = 200 
                 }
 ]


}
