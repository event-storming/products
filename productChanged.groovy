package contracts.messaging
org.springframework.cloud.contract.spec.Contract.make {
    description("""
spring contract 에서 메세지를 받는 방식은 총 3가지인데,
1. input 은 없고 output만 있는 경우
2. input 을 받아서 output 으로 보내는 경우
3. input 만 있는 경우

input 에 triggeredBy method 를 호출하는 경우는 보통 input 메세지가 없는 경우이다.

```
given:
	product changed event occurred
when:
	he applies for a beer
then:
	we'll send a message with a ProductChanged message
```

""")
    // Label by means of which the output message can be triggered
    label 'productChanged'
    // input to the contract
    input {
        // the contract will be triggered by a method
        triggeredBy('productChanged()')
    }
    // output message of the contract
    outputMessage {
        // destination to which the output message will be sent
        sentTo 'eventTopic'
        // the body of the output message
        body(
                eventType: "ProductChanged",
                productId: 1,
                productName: "TV",
                productPrice: 10000,
                productStock: 10,
                imageUrl: "testUrl"
        )
        bodyMatchers {
            jsonPath('$.eventType', byRegex("ProductChanged"))
            jsonPath('$.productId', byRegex(nonEmpty()).asLong())
            jsonPath('$.productName', byRegex(nonEmpty()).asString())
            jsonPath('$.productPrice', byRegex(nonEmpty()).asLong())
            jsonPath('$.productStock', byRegex(nonEmpty()).asLong())
            jsonPath('$.imageUrl', byRegex(nonEmpty()).asString())
        }
        headers {
            messagingContentType(applicationJson())
        }
    }
}
