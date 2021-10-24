# Design Document

## Major Design Decisons
### Session
Session uses the **Builder** design pattern. The Builder design pattern was chosen to reduce the complexity of Session constructor calls. For example, some sessions take place in a classroom, some are online, some have start and end times, some are asynchronous. Using Builder allows a Session to be 'built' piece-by-piece, using only information relevant to that specific lecture or tutorial.


