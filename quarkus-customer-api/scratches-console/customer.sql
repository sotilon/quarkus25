2025-02-16 03:30:38 INFO exec -a "java" java -XX:MaxRAMPercentage=80.0 -XX:+UseParallelGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:+ExitOnOutOfMemoryError -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -cp "." -jar /deployments/quarkus-run.jar
2025-02-16 03:30:38 INFO running in /deployments
2025-02-16 03:30:40 __  ____  __  _____   ___  __ ____  ______
2025-02-16 03:30:40  --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
2025-02-16 03:30:40  -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
2025-02-16 03:30:40 --\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2025-02-16 03:30:40 2025-02-16 08:30:40,469 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.hibernate-reactive.dialect" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     select
2025-02-16 03:30:41         *
2025-02-16 03:30:41     from
2025-02-16 03:30:41         information_schema.sequences
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     select
2025-02-16 03:30:41         table_catalog as table_cat ,
2025-02-16 03:30:41         table_schema as table_schem ,
2025-02-16 03:30:41         table_name as table_name ,
2025-02-16 03:30:41         table_type as table_type ,
2025-02-16 03:30:41         null as remarks
2025-02-16 03:30:41     from
2025-02-16 03:30:41         information_schema.tables
2025-02-16 03:30:41     where
2025-02-16 03:30:41         1 = 1
2025-02-16 03:30:41         and table_name like $1
2025-02-16 03:30:41         and table_type in ( $2, $3, $4, $5 )
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     select
2025-02-16 03:30:41         table_name as table_name,
2025-02-16 03:30:41         column_name as column_name,
2025-02-16 03:30:41         case
2025-02-16 03:30:41             when udt_name = 'bpchar'
2025-02-16 03:30:41                 then 'CHAR'
2025-02-16 03:30:41             else udt_name
2025-02-16 03:30:41     end as type_name,
2025-02-16 03:30:41     null as column_size,
2025-02-16 03:30:41     null as decimal_digits,
2025-02-16 03:30:41     is_nullable as is_nullable,
2025-02-16 03:30:41     null as data_type
2025-02-16 03:30:41 from
2025-02-16 03:30:41     information_schema.columns
2025-02-16 03:30:41 where
2025-02-16 03:30:41     1 = 1
2025-02-16 03:30:41 order by
2025-02-16 03:30:41     table_catalog,
2025-02-16 03:30:41     table_schema,
2025-02-16 03:30:41     table_name,
2025-02-16 03:30:41     column_name,
2025-02-16 03:30:41     ordinal_position
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     create table Customer (
2025-02-16 03:30:41         id bigint not null,
2025-02-16 03:30:41         accountNumber varchar(255),
2025-02-16 03:30:41         address varchar(255),
2025-02-16 03:30:41         code varchar(255),
2025-02-16 03:30:41         names varchar(255),
2025-02-16 03:30:41         phone varchar(255),
2025-02-16 03:30:41         surname varchar(255),
2025-02-16 03:30:41         primary key (id)
2025-02-16 03:30:41     )
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     create table Product (
2025-02-16 03:30:41         id bigint not null,
2025-02-16 03:30:41         product bigint,
2025-02-16 03:30:41         customer bigint,
2025-02-16 03:30:41         primary key (id)
2025-02-16 03:30:41     )
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     alter table if exists Product
2025-02-16 03:30:41        drop constraint if exists UK3sholf9ejwyd14jwe21ypksr1
2025-02-16 03:30:41 2025-02-16 08:30:41,889 WARN  [io.ver.sql.imp.SocketConnectionBase] (vert.x-eventloop-thread-2) Backend notice: severity='NOTICE', code='00000', message='constraint "uk3sholf9ejwyd14jwe21ypksr1" of relation "product" does not exist, skipping', detail='null', hint='null', position='null', internalPosition='null', internalQuery='null', where='null', file='tablecmds.c', line='11704', routine='ATExecDropConstraint', schema='null', table='null', column='null', dataType='null', constraint='null'
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     alter table if exists Product
2025-02-16 03:30:41        add constraint UK3sholf9ejwyd14jwe21ypksr1 unique (customer, product)
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     create sequence Customer_SEQ start with 1 increment by 50
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     create sequence Product_SEQ start with 1 increment by 50
2025-02-16 03:30:41 [Hibernate]
2025-02-16 03:30:41     alter table if exists Product
2025-02-16 03:30:41        add constraint FKqnwbub3jrvsb86gxqnxcvcta5
2025-02-16 03:30:41        foreign key (customer)
2025-02-16 03:30:41        references Customer
2025-02-16 03:30:41 2025-02-16 08:30:41,917 WARN  [io.qua.hib.orm.run.ser.QuarkusRuntimeInitDialectFactory] (JPA Startup Thread) Persistence unit default-reactive: Could not retrieve the database version to check it is at least 12.0.0
2025-02-16 03:30:42 2025-02-16 08:30:42,022 INFO  [io.quarkus] (main) ms-quarkus-customer-api 1.0.0 on JVM (powered by Quarkus 3.18.3) started in 3.304s. Listening on: http://0.0.0.0:8082
2025-02-16 03:30:42 2025-02-16 08:30:42,023 INFO  [io.quarkus] (main) Profile prod activated.
2025-02-16 03:30:42 2025-02-16 08:30:42,023 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, hibernate-reactive, hibernate-reactive-panache, hibernate-validator, jdbc-postgresql, narayana-jta, reactive-pg-client, rest, rest-client, rest-client-jackson, rest-jackson, smallrye-context-propagation, smallrye-fault-tolerance, smallrye-graphql, smallrye-graphql-client, smallrye-openapi, swagger-ui, vertx]

