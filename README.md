= API Rate Limiter Sample
Spring Boot Starter for RateLimit API calls with help of Bucket4j.

Some basic concepts for better understanding of bucket4J logic


== Basic information
* [Token bucket wikipedia](https://en.wikipedia.org/wiki/Token_bucket) - wikipedia page describes the token-bucket algorithm in classical form.
* [Non-formal overview of token-bucket algorithm](doc-pages/token-bucket-brief-overview.md) - the brief overview of token-bucket algorithm.


== Contents

* <<introduction>>
* <<getting_started>>
* <<bucket4j_complete_properties>>
* <<configuration_examples>>


[[introduction]]
== Introduction

This project is a Sample Project for Limit API calls based on different conditions.
It can be used limit the rate of access to your REST APIs based on URLs and IP-Address basis.

* Prevention of DoS Attacks for signin or any other open REST Points
* Request throttling for your crucial EndPoints.

The benefit of this project is the configuration of Bucket4j via Spring Boots *properties* or *yaml* files. We dont need to write any code for it.
.

[[getting_started]]
== Getting started

To use the rate limit in your project you have to add the Bucket4j Spring Boot Starter dependency in 
your project. 
[source, xml]
----
<dependency>
				<groupId>com.giffing.bucket4j.spring.boot.starter</groupId>
				<artifactId>bucket4j-spring-boot-starter</artifactId>
				<version>0.2.0</version>
			</dependency>
			<dependency>
				<groupId>com.github.vladimir-bukhtoyarov</groupId>
				<artifactId>bucket4j-core</artifactId>
				<version>4.10.0</version>
			</dependency>
			<dependency>
				<groupId>com.github.vladimir-bukhtoyarov</groupId>
				<artifactId>bucket4j-jcache</artifactId>
				<version>4.10.0</version>
			</dependency>
		<dependency>
			<groupId>javax.cache</groupId>
			<artifactId>cache-api</artifactId>
		</dependency>
		<dependency>
			<groupId>com.github.ben-manes.caffeine</groupId>
			<artifactId>caffeine</artifactId>
		</dependency>
		<dependency>
			<groupId>com.github.ben-manes.caffeine</groupId>
			<artifactId>jcache</artifactId>
			<version>2.5.3</version>
		</dependency>
----



> Don't forget to enable the caching feature by adding the @EnableCaching annotation to any of the configuration classes. This should be done in your main class.

Sample code to enable cache
----

@SpringBootApplication
@EnableCaching
public class RateLimiterBucket4JApplication {

----

The configuration can be done in the application.properties / application.yml. 
The following configuration limits all requests independently from the user. It allows a maximum of 5 requests within 10 seconds independently from the user.


[source,yml]
----
bucket4j:
  enabled: true
  filters:
  - cache-name: buckets
    url: .*
    rate-limits:
    - bandwidths:
      - capacity: 5
        time: 10
        unit: seconds
----

For Ehcache 3 you also need a *ehcache.xml* which can be placed in the classpath.
The configured cache name *buckets* must be defined in the configuration file.   

[source,yml]
----
spring:
  cache:
    jcache:
      config: classpath:ehcache.xml
----

[source,xml]
----
<config xmlns="...">
	<cache alias="buckets">
		<expiry>
			<ttl unit="seconds">3600</ttl>
		</expiry>
		<heap unit="entries">1000000</heap>
	</cache>

</config>
----

[[bucket4j_complete_properties]]
== Bucket4j properties

These properties can be added to any property file


[source, properties]
----
#we need to configure cache which will be used by Bucket4J
spring.cache.cache-names=rate-limit-buckets            
spring.cache.caffeine.spec=maximumSize=100000,expireAfterAccess=3600s

# enable/disable bucket4j support
bucket4j.enabled=true 
bucket4j.filters[0].filter-method=servlet
bucket4j.filters[0].cache-name=rate-limit-buckets
bucket4j.filters[0].url=.*
bucket4j.filters[0].rate-limits[0].expression=getRemoteAddr()# This will check remote address and use bucket for it
bucket4j.filters[0].rate-limits[0].execute-condition=@rateLimitService.isRateLimitRequired(getRequestURL())# we are using service to handle different url case incase you dont want to create multiple filter
bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity=10
bucket4j.filters[0].rate-limits[0].bandwidths[0].time=30
bucket4j.filters[0].rate-limits[0].bandwidths[0].unit=seconds
bucket4j.filters[0].rate-limits[0].bandwidths[0].fixed-refill-interval=30
bucket4j.filters[0].rate-limits[0].bandwidths[0].fixed-refill-interval-unit=seconds


----

=== Filter types

Filter types are predefined configuration option on how to define the key which should be used to limiting the requests.

==== Default

The default options doesn't differentiates between incoming requests (user, ip, etc). Its a general limiting.

==== IP

The IP filter type limits the access based on the IP address (httpServletRequest.getRemoteAddr()). So each IP address will independently throttled.

==== Expression

The expression based filter type provides the most flexible one and uses the https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html[Spring Expression Language] (SpEL). https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html#expressions-spel-compilation[The expression compiles to a Java class which will be used].
It provides an easy way to configure the throttling in different environments without writing one line of code.

Depending on the filter method [servlet,webflux,gateway] different SpEL root objects object can be used in the expression so that you have a direct access to the method of these request objects:

* servlet: javax.servlet.http.HttpServletRequest (e.g. getRemoteAddr() or getRequestURI())
* webflux: org.springframework.http.server.reactive.ServerHttpRequest
* gateway: org.springframework.http.server.reactive.ServerHttpRequest

*Limiting based on IP-Address*:
[source]
----
getRemoteAddr() #use this method only as getRemoteAddress() will return error while running your application
----