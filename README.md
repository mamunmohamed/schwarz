<h1>Introduction</h1>

This guide will provide an explanation of the schwarz application's bad practices, or detected bugs, or possible violations of the principles.

<h2>Defining the architecture for the project</h2>

When the development of a Back-end application begins, the choice of the technological stack, whether it will be a microservice or monolithic, whether a messaging queue will be used for this purpose, 
is made as to how the project will be structured. In other words, the architecture on which the project will be organized. The purpose is that this way it will be easy to separate the business logic, 
communication with the outside, and easy to maintain as the project grows. In addition, if an architecture is used to define the structure of the project, then if a new person joins in the future,
it will be easy for them to understand the project at the structure level.

Currently, the project does not have any clear structure. Taking this situation into account, the Hexagonal architecture has been proposed.

<img width="603" alt="Screenshot 2024-11-19 at 14 21 16" src="https://github.com/user-attachments/assets/f527113b-0463-48a4-ade2-0fcc07d6be51">

<h2>Application.yml</h2>

In the application.yml file we can see that the database credentials such as url, username and password are publicly exposed. This is a bad practice, since a user with bad intentions could try to cause damage.
The data for these parameters should be read from the environment variables.


<img width="607" alt="Screenshot 2024-11-19 at 14 31 45" src="https://github.com/user-attachments/assets/9e7e3f90-1d84-4ab4-bb4b-8910a1b55d81">


<h3>Solution</h3>


<img width="1043" alt="Screenshot 2024-11-19 at 14 32 51" src="https://github.com/user-attachments/assets/56ec507d-2195-4f43-b8f5-5c702b528b85">

<h2>DTO Pattern</h2>
As you can see in the ApplicationRequestDTO class, a Basket type attribute has been created. Basket is a domain object, and there cannot be a domain object attribute in a DTO class.

According to the DTO pattern, its purpose is to exchange data between layers and with the outside. There cannot be a domain object attribute within a DTO. Because, it would violate the principle of the pattern, and would generate issues such as:
<ul>
  <li>It would create a strong dependency with external layers.</li>
  <li>Any change made to the domain would force its dependents to modify their implementation.</li>
  <li>It does not respect the separation of responsibility of each layer. That is, a Domain object is responsible for the business logic, and instead DTO has no business, it is simply used to transfer data.</li>
</ul>




![image](https://github.com/user-attachments/assets/8da543df-5598-4847-bfa1-b650c5448893)

<h3>Correction</h3>

Create a DTO class for the Basket domain object.

![image](https://github.com/user-attachments/assets/3f27abc9-1c25-48ac-9577-9ef565b7e2a5)

<h2>Coupon Resource errors</h2>

In the CouponResource class, it has been detected that the 3 defined endpoints have some errors. The error and its solution are mentioned below.

The first error that has been considered is related to the nomenclature of the methods. It can be observed that the first and second methods,
which are apply and create, only use the verb compared to the third method that adds the verb get and then the word coupon. When defining the nomenclatures,
it must be taken into account that all the names of the methods have to follow a nomenclature for easy understanding.

![image](https://github.com/user-attachments/assets/0a437641-fc42-4a30-aba8-939b72d21bd1)

<h3>Correction</h3>

The word coupon will be added after each verb. E.g.: applyCoupon, createCoupon, getCoupons.

![image](https://github.com/user-attachments/assets/431f2d4c-e1ae-4908-a460-0a9312cfab5b)

The second error that has been detected is that, in the first and third method, they are returning the domain object, and this is an error.

It is a bad practice to use the same object for different functions. This process can cause several problems, among them could be: Strong coupling between layers, since, if any change is made,
it will have to be modified in all locations. Yes, an Entity object is used to save in the database, and then to answer a call,
it would be exposing the implementation of the Database, and this could make it easier for attackers to carry out malicious attacks.

<h4>First method</h4>

![image](https://github.com/user-attachments/assets/0898e31e-283d-4a10-9282-340a70d08c16)

<h4>Correction</h4>

Replace the domain object as response with the BasketDTO object.

![image](https://github.com/user-attachments/assets/2c2e7106-2cec-46b3-9cc9-a444f9cf33e5)

![image](https://github.com/user-attachments/assets/58878180-2480-4442-aac5-f93339d7fbeb)

<h4>Third method</h4>

Although this method does not return the Coupon object as a response, it returns a method of the service class that returns the Coupon object.

![image](https://github.com/user-attachments/assets/ff8d8710-2b82-44d6-92ab-4c299448570d)

<h4>Correction</h4>

Replace the domain object as response with the CouponDTO object.

![image](https://github.com/user-attachments/assets/7b54cc85-cb60-4bcb-9646-62f1e958abf9)

![image](https://github.com/user-attachments/assets/37d3f01c-c5e9-487f-9f19-8074194ad0d2)


After correcting the response objects, the application will start to break due to changes made. Since the implementation has been modified.

When adapting the existing implementation to new code, it can be observed that the flow of each method presents some errors. The error is explained below, as well as the solution.

In the CouponResource class, the methods receive the object by body, and delegate directly to the business layer. Then, the business layer processes the data according to its need.
This solution is valid in small projects. However, as the project grows, it is optimal to opt for the solution of using a mapper.

The mapper separates the responsibility of the transformation, allows the same code to be reused in different layers, easy to maintain when it grows.

<h4>First method apply</h4>

As can be seen, the applicationRequestDTO.getBasket() object received by body is being delegated directly.

![image](https://github.com/user-attachments/assets/354fe387-eab8-4535-a663-c64290dd1f90)

Following the flow of the apply method of the CouponResource class, in the CouponService service layer it can be observed that the apply method that has been invoked has some errors such as:

<ul>
  <li>It can be observed that unnecessary checks are being performed.</li>
  <li>The else if could be omitted, since it is checking if the doubleValue value is equal to 0. Instead of checking, the action could simply be returned with a return basket. It has that, if the value is 0, then no discount will be applied.</li>
  <li>The System.out.println is seen to be used to display a DEBUG message. This way is not correct, since, in local development it could be accepted, but in production to deal with the error it is not the way. In this way I would have problems when monitoring with tools such as Grafana.</li>
  <li>The nomenclature of the method that follows does not match the others.
</li>
</ul>


![image](https://github.com/user-attachments/assets/87d5aab6-f6e6-48a8-ab8a-f51d743421b4)

<h4>Correction</h4>

As a solution, a mapper would be implemented to map from BasketDTO (this object has been defined in the new implementation) to Basket to delegate to the service.
Then, create another mapper to convert from Basket to BasketDTO to return the response of the request.

Mapper definition.

![image](https://github.com/user-attachments/assets/cf52afa2-aaf5-4eda-933b-97529c1af0e4)

After the definition, an instance of Basket Mapper has been created in the CouponResource class to return the transformed BasketDTO object in Basket, and then delegate to the service layer. And vice versa to return the request response.

![image](https://github.com/user-attachments/assets/c9bf9499-2051-41dd-bf99-147de5b5f522)

In the CouponService class, solutions have been proposed for the detected errors.

<ul>
  <li>On line 35, System.out.println has been replaced.</li>
  <li>The else if check has been removed by else.</li>
  <li>The apply method has been renamed to applyCoupon.</li>
</ul>



![image](https://github.com/user-attachments/assets/3a5a09cf-fc3c-438b-b102-7868d5ce1379)

The second createCoupon method, after analysis, has been detected to have a Coupon type variable declared that saves the createCoupon response that is not being used for anything. 
It is also delegating the CouponDTO object that it receives by body to service.


![image](https://github.com/user-attachments/assets/bf5875ce-17a3-405a-bfd9-4aef4322fd79)

Following the flow of this method, the createCoupon method of the CouponService class has some errors that are described below:
<ul>
  <li>It is constructing a Coupon object, and it tries to throw a NullPointerException. However, it has no implementation. The comment that is there has no effect.</li>
  <li>The way it handles the NullPointerException is to control the null of the code attribute. Because, in this way, the error is being generalized without specifying the field, and this can be tedious when it comes to finding the error.</li>
  <li>On line 65, it is saving the coupon object in the database. As such, it would be saving a null object.</li>
  <li>Since the method is only saving a Coupon, then it does not have to return anything. It could be a void method.</li>
  <li>The same Coupon domain object is being used to save to the database, and it is being used to return the response of the method.</li>
</ul>






![image](https://github.com/user-attachments/assets/96eca830-13df-4e2d-b0d2-9253d19e7933)

Correction

Based on the problems described above, the following solution has been proposed:

As a first step, a mapper class has been created to map from CouponDTO to Coupon to delegate to the service. Then, one more mapper has been created to convert from Coupon to CouponDTO to return the response of the request. Also, two more mappers have been created to convert from Coupon to CouponEntity.

Mapper definition.

![image](https://github.com/user-attachments/assets/ad633a55-5a90-4864-85b1-fcf06277e2e9)

An instance of CouponMapper has been created in the Coupon Resource class. The mapper will then be used to convert the CouponDTO object to Coupon, and then pass it to the service class.


<img width="603" alt="Screenshot 2024-11-19 at 15 00 46" src="https://github.com/user-attachments/assets/5f9479d9-a34e-4de8-a23f-cd6f24459acd">

In the createCoupon method of the CouponService class, the following solutions have been modified:
In the solution, the object that receives params has been changed to Coupon. In this way, the received object will not have to be transformed to build the object again to save in the database.
The try/catch block has been removed for the if condition. It is checked if the Coupon code that is received is null or not. If yes, the exception is thrown. Otherwise, it is saved in the database.
The method has been converted to void.
A new object of type Entity called CouponEntity has been created to interact with the database.
In the CouponRepository class, the Coupon object has been changed to CouponEntity to save in the database.

![image](https://github.com/user-attachments/assets/4a83ed3b-dccb-44b9-bd2b-07ced1fd9709)

![image](https://github.com/user-attachments/assets/fb83128e-b32a-4173-bbfa-89b977a6a194)

The third method is getCoupons of the CouponResource class. After studying the flow, it has been seen that it has some errors.
When changing the type of data that will be returned as a response, it is observed that the method starts to complain. Because it does not match the previous implementation.
It delegates the CouponRequestDTO object. This is a bad practice, since passing an untransformed DTO object is not the correct way in large projects.

![image](https://github.com/user-attachments/assets/5219f747-7b8d-49f8-b7c9-ebcd3d6154ac)

Within the flow, in the CouponService class, the getCoupons method has some errors that are described below:

<ul>
  <li>It does not check if the CouponRequestDTO object it has received, the getCodes() property is null or not.</li>
  <li>It makes a call to the repository for each code. This would affect the performance of the application.</li>
</ul>



![image](https://github.com/user-attachments/assets/fefe69ec-2ba1-499c-8474-cb8c02920e9a)

<h4>Correction</h4>

In the getCoupons method of the CouponResource class, the following solutions have been proposed:

A CouponRequest domain object has been created to transform the DTO object received by body.

![image](https://github.com/user-attachments/assets/2e9c8340-cf87-417c-8002-80b64abaf6e2)

Three more methods have then been created in the CouponMapper class to convert CouponRequestDTO to CouponRequest, Coupon list to CouponDTO list and CouponEntity list to Coupon list.

![image](https://github.com/user-attachments/assets/a72ccadf-3e88-436e-b918-3514a70b224d)

After the methods were created in mapper, the method to perform the object conversion has been invoked.

![image](https://github.com/user-attachments/assets/d8edac78-9ba4-40bf-8a54-6640546dff7e)

In the CouponService class, the following solutions have been proposed in the getCoupons method:
<ul>
  <li>Check whether the object received is null or not, and whether the codes array is empty</li>
  <li>Replace the CouponRequestDTO with CouponRequest</li>
  <li>Create a new method called findCouponsByCodeIn in the repository with a custom query to obtain all the coupons according to the code received.</li>
  <li>A mapper method has been implemented to convert the CouponEntity list to a Coupon list.</li>
</ul>


![image](https://github.com/user-attachments/assets/06173d87-90fa-456c-b43b-5b871fb761ef)

Implementation of the new method in the Coupons Repository with a custom query.

![image](https://github.com/user-attachments/assets/abd309a9-b20a-4ef2-ac8c-2ab134263e1e)





































