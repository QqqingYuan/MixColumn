# MixColumn

清洗数据表

## Introduction

### Transform

单表内列值变换。

### Adjunction

从一个表向另一个表添加列。

### Merge

单表内两列组合生成新列。


## Usage

java -jar BuildMixColumn.jar  jdbc-config  [transform-jar-file] .

## Configuration

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

# merge
mixcolumn.merge.table=scholarlist
mixcolumn.merge.new_table=test
mixcolumn.merge.columns=scholarname,colleage
mixcolumn.merge.column_new=combine
mixcolumn.merge.transform=test.combineFilter

```
