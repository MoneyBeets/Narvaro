# Narvaro MySQL Database Schema
#
# Narvaro: @VERSION@
# Build Date: @DATE@
# Commit Head: @HEAD@
# JDK: @JDK@
# ANT: @ANT@
#

# Table holds Narvaro version data
#   Used for schema upgrades
CREATE TABLE narvaroVersion
(
    name    VARCHAR(50) NOT NULL,
    version INTEGER NOT NULL,
    PRIMARY KEY (name)
);

# insert default table values
INSERT INTO narvaroVersion (name, version) VALUES ('narvaro', 1);

# Table holds park names
CREATE TABLE parks
(
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

# Table holds 449 Form binary blobs
CREATE TABLE forms
(
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(80),
    form MEDIUMBLOB
);

# This table holds monthly park data
#
# Prefix Key:
#     pdu = Paid Day Use
#     fdu = Free Day Use
#     fsc = Free Safety Classes
#     o   = Other
CREATE TABLE data
(
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    park INT UNSIGNED,
    month DATE,
    pduConversionFactor DECIMAL(8,4),
    pduTotals INT UNSIGNED,
    pduSpecialEvents INT UNSIGNED,
    pduAnnualDayUse INT UNSIGNED,
    pduDayUse INT UNSIGNED,
    pduSenior INT UNSIGNED,
    pduDisabled INT UNSIGNED,
    pduGoldenBear INT UNSIGNED,
    pduDisabledVeteran INT UNSIGNED,
    pduNonResOHVPass INT UNSIGNED,
    pduAnnualPassSale INT UNSIGNED,
    pduCamping INT UNSIGNED,
    pduSeniorCamping INT UNSIGNED,
    pduDisabledCamping INT UNSIGNED,
    fduConversionFactor DECIMAL(8,4),
    fduTotals INT UNSIGNED,
    fscTotalVehicles INT UNSIGNED,
    fscTotalPeople INT UNSIGNED,
    fscRatio DECIMAL(8,4),
    oMC INT UNSIGNED,
    oATV INT UNSIGNED,
    o4X4 INT UNSIGNED,
    oROV INT UNSIGNED,
    oAQMA INT UNSIGNED,
    oAllStarKarting INT UNSIGNED,
    oHangtown INT UNSIGNED,
    oOther INT UNSIGNED,
    comment TEXT,
    form449 INT UNSIGNED,
    FOREIGN KEY (park) REFERENCES (parks),
    FOREIGN KEY (form449) REFERENCES (forms)
);
