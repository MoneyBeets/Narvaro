# Narvaro MySQL Database Schema
#
# Narvaro: @VERSION@
# Build Date: @DATE@
# Commit Head: @HEAD@
# JDK: @JDK@
# ANT: @ANT@
#

CREATE TABLE narvaroVersion (
    name    VARCHAR(50) NOT NULL,
    version INTEGER NOT NULL,
    PRIMARY KEY (name)
);

# insert default table values
INSERT INTO narvaroVersion (name, version) VALUES ('narvaro', 1);
