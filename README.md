# DatabaseVersonControlUtil
A Java Util for Database Script Version Control

# 背景 Background

This module is built for exploring the possibilities of database schema version control. We are applying database version control practise and in meanwhile developing this module to hook with our CICD piplines or production release platforms.

本模块和标准是为了探索数据库版本控制而建立。

# 脚本管理标准 Script Management Standard

Standards are designed for code version based incrementally deploying or rolling back database schemas same way we do with our application services.

本标准为实现更直观方便的数据库增量部署，回滚及对应版本控制而设计。

## 类型和命名规范 Types and Naming Conversion

There will be 2 deployment types and 3 script types defined in script file name.

**部署类型 Deployment Type**:

- R : Release 正向部署
- U : Undo or Rollback 反向回滚（可选）

**脚本类型 Script Type**:

- DDL : Data Definition Language
- DML : Data Manipulation Language
- PATCH : Patch Script is a special type that isn't considered as part of the application database version, usually executed only once for fixing issue or correct certain data. 补丁类脚本，一般为修复指定问题或修正部分数据而准备，非数据库主版本的一部分。

**版本号 Version**:

Version format should be digits with delimiter dot(.). e.g: 1.0.2

为[.]间隔的数字串，如：1.0.2

**文件命名规范 Naming Conversion**:

Script file name should be ：

```
{Deployment Type: R|U}{version}_{script_type: DDL|DML|PATCH}_{script_name}.sql

e.g: 

- R1.0.0_DDL_CreateTable.sql
- R1.0.0_DML_AddData.sql
- R1.0.1_DDL_CreateTable2.sql
- U1.0.1_DDL_DROP.sql
```

# 脚本的自动执行顺序 Script Execution Order

**版本比较规则 Version Comparison**

Compare the digit position one by one, first larger digit considers as larger version.
If number of version digits are not same, fulfill zero to compare and apply same rule.

版本以数字位逐位比较，取大；如同位数字相等，则比较次位，取大；如位数不同，则以0补足参加比较，最终取大。

```
e.g result:

- 2.0.0 > 1.9.9
- 2.0.1 > 2.0.0
- 2.0.1.1 > 2.0.1
- 2.0.1.1 > 2.0.1.0.1
```

**执行顺序 Execution Order**

For **Release**

- Script will be executed follow version from small to large. 以版本号小向大正向选择脚本执行。
- Within same version, DDL will be executed before DML. 同版本号内部，DDL先于DML执行。
- If Patch included, Patch script will be executed after DML. 如果包含Patch脚本，则Patch脚本后于DML执行。
- Multiple DDL|DML|Patch within same version, order will be alphabetical. 如果同版本号内部存在多个DDL，DML及Patch类脚本，执行顺序一般按照字母排序。

# 例子 Example

It is not neccessary to use subfolders like ddl or dml. Only script file name matters.

实际并不要求按照ddl dml分文件夹管理，只有文件名是关键信息。最终实现的脚本拾取逻辑应遍历所有子文件夹，对所有脚本类文件进行索引和排序。

```
├─ddl
│      R1.0.0_DDL_Table.sql
│      R1.0.1_DDL_Table.sql
│      R1.0.3_DDL_Table.sql
│      R1.0.3.1_DDL_AddCol.sql
│      R1.1.0_DDL_NewTable.sql
│
└─dml
        R1.0.0.1_DML_InsertMore.sql
        R1.0.0_DML_InsertData.sql
        R1.0.2_DML_RemoveData.sql
        R1.0.3.1_PATCH_Delete.sql
        R1.1.0_DML_MetaData.sql
```

**Release from 1.0.0 - 1.0.0**:

- ddl/R1.0.0_DDL_Table.sql
- dml/R1.0.0_DDL_InsertData.sql
- dml/R1.0.0.1_DML_InsertMore.sql

**Release from 1.0.1 - 1.1**:

- ddl/R1.0.1_DDL_Table.sql
- dml/R1.0.2_DML_RemoveData.sql
- ddl/R1.0.3_DDL_Table.sql
- ddl/R1.0.3.1_DDL_AddCol.sql
- ddl/R1.1.0_NewTable.sql
- dml/R1.1.0_MetaData.sql

**Release from 1.0.1 - 1.1 including Patch**:

- ddl/R1.0.1_DDL_Table.sql
- dml/R1.0.2_DML_RemoveData.sql
- ddl/R1.0.3_DDL_Table.sql
- ddl/R1.0.3.1_DDL_AddCol.sql
- dml/R1.0.3.1_PATCH_Delete.sql
- ddl/R1.1.0_NewTable.sql
- dml/R1.1.0_MetaData.sql
  
# TODO

- [X] Complete core
- [X] Interfaces
- [X] Runtime
- [ ] UT
- [ ] Variables injection
