FROM docker.io/amazoncorretto:17-alpine

# create non-root user and group
# -l and static IDs assigned to avoid delay in lookups and system logging
ARG THE_USER_ID=1001
ARG THE_GROUP_ID=1001

RUN /usr/sbin/addgroup -g $THE_GROUP_ID spring && \
/usr/sbin/adduser -D -u $THE_USER_ID -G spring -g $THE_GROUP_ID spring && \
mkdir logs && chgrp spring logs && chmod ug+rwx logs

# run as non-root
USER spring:spring

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080

COPY <%=name%>-<%=version%>.jar app.jar
CMD ["java","-jar","app.jar"]