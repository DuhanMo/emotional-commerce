## 유저 도메인

```mermaid
classDiagram
    class User {
        -Long id
        -String name
        -String email
        -UserStatus status
    }
    
    class UserStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
    }
    
    class Point {
        -Long id
        -Long userId
        -Int amount
        +charge()
    }
    
    class PointLog {
        -Long id
        -Long userId
        -Long pointId
        -Int amount
    }
    
    User --> UserStatus : has 
    Point --> User
    PointLog --> User
    PointLog --> Point
```

# 상품 도메인

```mermaid
classDiagram
    %% 브랜드
    class Brand {
        -Long id
        -String name
        -String description
        -BrandStatus status
        +isActive()
        +validateExists()
        +canDisplayProducts()
    }
    
    class BrandStatus {
        <<enumeration>>
        ACTIVE
        CLOSED
    }
    
    %% 상품
    class Product {
        -Long id
        -Long brandId
        -Int stock
        -Long price
        -String name
        -String imageUrl
        -ProductStatus status
        -Boolean deleted
        +increaseStock(quantity)
        +decreaseStock(quantity)
    }
    
    class ProductStatus {
        <<enumeration>>
        ON_SALE
        SOLD_OUT
    }
    
    class ProductLike {
        -Long id
        -Long userId
        -Long productId
        -Boolean deleted
        +isDeleted()
        +delete()
        +restore()
    }
    
    %% 상품 집계
    class ProductSummary {
        -Long id
        -Long productId
        -Int likeCount
        +increaseLikeCount()
        +decreaseLikeCount()
    }
    
    %% 관계
    Product --> Brand
    Product --> ProductStatus : has
    Brand --> BrandStatus : has
    
    ProductLike --> Product
    ProductSummary --> Product
```

## 주문 도메인

```mermaid
classDiagram
    %% 주문
    class Order {
        -Long id
        -Long userId
        -OrderStatus status
        -Address deliveryAddress
        -Int totalAmount
        -LocalDateTime orderedAt
    }
    
    %% 주문 상품
    class OrderLine {
        -Long id
        -Long orderId
        -Long productId
        -Int quantity
        -Int unitPrice
        -Int lineAmount
    }
    
    %% 값 객체들
    class Address {
        -String street
        -String city
        -String zipCode
        -String detailAddress
    }
    
    class OrderStatus {
        <<enumeration>>
        PENDING
        PAID
        PAYMENT_FAILED
        CANCELLED
        SHIPPED
        DELIVERED
    }
    
    %% 관계
    OrderLine --> Order
    Order --> Address : has
    Order --> OrderStatus : has
```

## 결제 도메인

```mermaid
classDiagram
    class Payment {
        -Long id
        -Long orderId
        -Long userId
        -Int amount
        -PaymentStatus status
        -String transactionId
        -LocalDateTime paidAt
    }
    
    class PaymentMethod {
        <<enumeration>>
        POINT
    }
    
    class PaymentStatus {
        <<enumeration>>
        PENDING
        SUCCESS
        FAILED
        CANCELLED
        REFUNDED
    }
    
    %% 관계
    Payment --> PaymentMethod : has
    Payment --> PaymentStatus : has
```

## 도메인간 연관관계

```mermaid
classDiagram
    %% 도메인별 대표 클래스
    class User {
        -Long id
    }
    
    class Product {
        -Long id
        -Long brandId
    }
    
    class Brand {
        -Long id
    }
    
    class ProductLike {
        -Long userId
        -Long productId
    }
    
    class ProductSummary {
        -Long productId
    }
    
    class Order {
        -Long id
        -Long userId
    }
    
    class OrderLine {
        -Long orderId
        -Long productId
    }
    
    class Payment {
        -Long orderId
        -Long userId
    }
    

    ProductLike --> User
    ProductLike --> Product
    ProductSummary --> Product
    
    Order --> User
    OrderLine --> Order
    OrderLine --> Product
    
    Payment --> Order
    Payment --> User
    
    Product --> Brand
```