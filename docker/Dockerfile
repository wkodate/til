FROM centos
MAINTAINER wkodate <hbwandeow@gmail.com>
# RUN: buildするときに実行される
# RUN echo "now building..."
# CMD: runするときに実行される
# CMD: echo "now running..."
# CMD ["echo", "now running..."]
RUN yum update -y
RUN yum install -y httpd
ADD ./index.html /var/www/html/
EXPOSE 80
CMD ["/usr/sbin/httpd". "-D", "FOREGROUND"]
