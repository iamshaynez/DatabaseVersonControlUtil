# DatabaseVersonControlUtil
A Java Util for Database Script Version Control

# Background

This module is built for exploring the possibilities of database schema version control. We are applying database version control practise and in meanwhile developing this module to hook with our CICD piplines or production release platforms.

# Script Management Standard

Standards are designed for code version based incrementally deploying or rolling back database schemas same way we do with our application services.

## Types and Naming Conversion

There will be 2 deployment types and 3 script types defined in script file name.

**Deployment Type**:

- R : Release
- U : Undo or Rollback

**Script Type**:

- DDL : Data Definition Language
- DML : Data Manipulation Language
- PATCH : Patch Script is a special type that isn't considered as part of the application database version, usually executed only once for fixing issue or correct certain data.

**Version**:

Version format should be digits with delimiter dot(.). e.g: 1.0.2

**Naming Conversion**:

Script file name should be ：

```
{Deployment Type: R|U}{version}_{script_type: DDL|DML|PATCH}_{script_name}.sql

e.g: 

- R1.0.0_DDL_CreateTable.sql
- R1.0.0_DML_AddData.sql
- R1.0.1_DDL_CreateTable2.sql
- U1.0.1_DDL_DROP.sql
```

# Script Execution Order

**Version Comparison Rule**

Compare the digit position one by one, first larger digit considers as larger version.
If number of version digits are not same, fulfill zero to compare and apply same rule.

```
e.g result:

- 2.0.0 > 1.9.9
- 2.0.1 > 2.0.0
- 2.0.1.1 > 2.0.1
- 2.0.1.1 > 2.0.1.0.1
```

**Execution Order**

For **Release**

- Script will be executed follow version from small to large.
- Within same version, DDL will be executed before DML. 
- If Patch included, Patch script will be executed after DML.
- Multiple DDL|DML|Patch within same version, order will be alphabetical.

# Example

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

- [ ] Complete core
- [ ] Interfaces
- [ ] Variables injection
