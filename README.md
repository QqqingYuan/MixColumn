# MixColumn

清洗数据表

## Usage

java -jar BuildMixColumn.jar  jdbc-config  [transform-jar-file] .

## 配置文件

```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://host:3306/database?characterEncoding=utf8
jdbc.username=username
jdbc.password=password

# MixColumn type
mixcolumn.type=transform

# transform
mixcolumn.transform.table=papercontent
mixcolumn.transform.new_table=paper
mixcolumn.transform.column=authors,pauthors
mixcolumn.transform.class=test.authorFilter,test.authorFilter

# adjunction
mixcolumn.adjunction.key_table=addtest
mixcolumn.adjunction.side_table=coscholarlist
mixcolumn.adjunction.key_identity_column=courl
mixcolumn.adjunction.side_identity_column=url
mixcolumn.adjunction.side_add_columns=coscholar,copapernum
mixcolumn.adjunction.new_key_table=addtest2

```
