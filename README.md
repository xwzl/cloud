# Linux 部署项目
    
    # pull 项目
    git clone https://github.com/xwzl/cloud.git
    
    # 运行项目
    cd ./cloud/spring-server
    
    # 集群
    docker run --rm  -e "SPRING_PROFILES_ACTIVE=server1" -p 8761:8761 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server1 -d cloud/spring-server:1.0.0
    docker run --rm  -e "SPRING_PROFILES_ACTIVE=server2" -p 8762:8762 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server2 -d cloud/spring-server:1.0.0
    docker run --rm  -e "SPRING_PROFILES_ACTIVE=server3" -p 8763:8763 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server3 -d cloud/spring-server:1.0.0  