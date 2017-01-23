USE [Personalization]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

--DROP OLD TABLES
-----------------------
IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL  DROP TABLE dbo.Product; 
IF OBJECT_ID('dbo.Category', 'U') IS NOT NULL  DROP TABLE dbo.Category; 
IF OBJECT_ID('dbo.Product_Category', 'U') IS NOT NULL  DROP TABLE dbo.Product_Category; 

CREATE TABLE [dbo].[Product](
	[ProductId] BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[ProductDesc] VARCHAR(450) NULL,
	[ProductCurrency] VARCHAR(450) NULL,
	[Price] VARCHAR(450) NOT NULL,
	[ProductCode] VARCHAR(450) NOT NULL,
	[ProductType] VARCHAR(450) NOT NULL,
	[UnitsInStock] [bigint] NOT NULL,
	[CreatedAt] [datetime] NOT NULL,
	[LastModifiedAt] [datetime] NOT NULL)

GO

CREATE TABLE [dbo].[Category](
	[CategoryId] BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[CategoryDesc] VARCHAR(450) NULL,
	[CategoryName] VARCHAR(450) NULL,
	[CreatedAt] [datetime] NOT NULL,
	[LastModifiedAt] [datetime] NOT NULL)
GO

CREATE TABLE [dbo].[Product_Category](
	[CategoryId] BIGINT NOT NULL,
	[ProductId] BIGINT NOT NULL,
	primary key (CategoryId, ProductId))
GO

CREATE TABLE [dbo].[CategoryParent](
	[Id] BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[CategoryId] [bigint] FOREIGN KEY REFERENCES Category(CategoryId),
	[ParentCategoryId] [bigint] NOT NULL)
GO