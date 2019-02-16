# KiddyMarket
The Market API is part of the Kiddy-enterprise software made for semester three software engineering at Fontys. It communicates with an external authentication API called KiddyBank. The KiddyMarket project is an API that uses the [KiddyInventory](https://github.com/spoilerdo/KiddyInventory) in order to keep track of the items obtained by a user.

### Core aspects
The core aspects of this API is that it can store offers that have been created by a user. It's also has transfer functionality so that 2 users can transfer items and money to each other. But a user can only buy as many items as it has offers, this in order to motivate users to use the [Casino](https://github.com/pietjan12/SpringRestApiS3) to keep buying new items. 

The API is made with Gradle and Spring and uses Hibernate as ORM.
