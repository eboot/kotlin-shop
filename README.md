# Kotlin Shop
[![Travis CI](https://travis-ci.org/iobruno/kotlin-shop.svg?branch=master)](https://travis-ci.org/iobruno/kotlin-shop)
[![Maintainability](https://api.codeclimate.com/v1/badges/3203ff55a8ce4d832e8d/maintainability)](https://codeclimate.com/github/iobruno/kotlin-shop/maintainability)
[![Known Vulnerabilities](https://snyk.io/test/github/iobruno/kotlin-shop/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/iobruno/kotlin-shop?targetFile=build.gradle)



This is just a pet project of mine to play with Kotlin stdlib

For this, I'm simulating an eCommerce platform as close as I can, model-wise. 
I took inspiration based on my understanding of how Amazon handles Physical, Digital
and Subscription Orders

## Up and Running

**Requirements**
- JDK 8+
- Gradle (build tool)

**Building**
```
./gradlew build
```

**Testing**
```
./gradlew test
```

## Usage
```
val shoppingCart = ShoppingCart()
                .addProduct(Product("PS4 Slim 1TB", Category.PHYSICAL, 1899.00), 1)
                .addProduct(Product("PDP Chair", Category.PHYSICAL, 399.00), 2)
                .addProduct(Product("Cracking the Code Interview", Category.PHYSICAL_BOOK, 219.57), 2)
                .addProduct(Product("The Hitchhiker's Guide to the Galaxy", Category.PHYSICAL_BOOK, 120.00), 1)
                .addProduct(Product("Stairway to Heaven", Category.DIGITAL_MUSIC, 5.00), 1)
                .addProduct(Product("Nier:Automata", Category.DIGITAL_VIDEO_GAMES, 129.90), 4)
                .addProduct(Product("Netflix Familiar Plan", Category.SUBSCRIPTION, 29.90), 1)
                .addProduct(Product("Spotify Premium", Category.SUBSCRIPTION, 14.90), 1)
                .addProduct(Product("Amazon Prime", Category.SUBSCRIPTION, 12.90), 1)

val orders = shoppingCart.checkout(account)

// Pick one or Iterate through the orders
order.place()
val invoice = order.pay()
order.fulfill()
order.complete() 
```

## Application Design

**Product, Category**

These are pretty self explanatory, right ? :)

**Item** 

Represents a given amount of Product in the `Shopping Cart` or in an `Order`, also the `Item Type`  is set 
according to the `Category` of the Product, as follows:
- Digital copies of Movies & TV, Music, Video Games or Software are set as `Digital`
- Subscriptions are set as `Membership`
- and all else (Books, Clothes, Electronics) are set as `Physical` items

**Order**
 
Represents an interface defining the implementations for  Physical, Digital and Membership Orders, 
each with its own set of rules for `place()`, `pay()`, `fulfill()` and `complete()` 

**PhysicalOrder** 

May only contain items of the `ItemType` 'Physical'

- `place()`: 
    - Setups the Packaging for Shipping                 
    - Includes a `Shipping and Handling` cost of extra $10 per package
    - Ready the order to the `PENDING` status     
    - **Note**: Provided that there are items that fall under the `PHYSICAL_BOOK` category, 
                they're grouped together into another shipment with the label `TAX_FREE`
    
- `pay()`:
    - Provided that the Order has been placed, and not yet payed:
    - `//TODO:` Process the Payment 
    - Generates an `Invoice`
    - Updates the OrderStatus to `UNSHIPPED`
        
- `fulfill()`:
    - Provided that the order has been payed and not yet shipped:
    -  `//TODO:` Notifies the seller to fulfill/process the Order on its end
    - Updates the OrderStatus to `SHIPPED`
        
- `complete()`:
    -  `// TODO:`: Track the packages/shipment they're all delivered
    - Updates the OrderStatus to `DELIVERED`
    
**Digital Order** 

May only contain items of the `ItemType` 'Digital'    

- `place()`: 
    - Includes a `Voucher` discount of $10 for the Order
    - Ready the order to the `PENDING` status     
    
- `pay()`:
    - Provided that the Order has been placed, and not yet payed:
    - `//TODO:` Process the Payment 
    - Generates an `Invoice`
    - Updates the OrderStatus to `UNSENT`
        
- `fulfill()`:
    - Provided that the order has been payed and not yet sent:
    -  `//TODO:` Notifies the seller to fulfill/process the Order on its end
    - Updates the OrderStatus to `SENT`
        
- `complete()`:
    -  `// TODO:`: Track when the the Buyer clicks on the emailed link to redeem the item
    - Updates the OrderStatus to `REDEEMED`    

**MembershipOrder**

May only contain items of the `ItemType` 'Membership'

- `place()`:
    - Ensures there's only one Subscription per Order
    - Ready the order to the `PENDING` status     
    - **Note**: In a scenario with multiple `Membership` Items in the Shopping Cart, 
    each will spawn a different Order
    
- `pay()`:
    - Provided that the Order has been placed, and not yet payed:
    - `//TODO:` Process the Payment 
    - Generates an `Invoice`
    - Updates the OrderStatus to `PENDING_ACTIVATION`
        
- `fulfill()`:
    - Provided that the order has been payed and not yet activated:
    - `//TODO:` Activates the Subscription Service
    - Updates the OrderStatus to `ACTIVATED`
        
**ShoppingCart**
 
To better simulate the user experience in an e-commerce platform, 
and also to wrap all the complexity of creating an Order, this Shopping Cart entity was created

- `addProduct(product: Product, n: Int)`:
    - Adds the N-amounts of the given `product` to the shopping Cart
    - **Note**: if the product is already in the Cart, adds up in the quantity
          
- `delete(product: Product)`: Deletes the product from the cart regardless of the quantity

- `subtotal()`: Computes the sum of (unittest price of each product in the Cart * quantity)
    
- `checkout(account: Account)`: 

    - All items that fall under `Physical` (regardless if their product category will be tax-free on Shipment or not) 
    are grouped together to create a `PhysicalOrder`    
    
    - All items that fall under `Digital` are grouped together to create a `DigitalOrder`   
    
    - Each item that falls under `Membership` will create a different Membership Order 
    (due to the complexity of activating each subscription individually, and probably through 3rd-party APIs) 
    
## TODOs
- [ ] Automate the building with Travis/Circle CI
- [ ] Build and ship with Graal VM
