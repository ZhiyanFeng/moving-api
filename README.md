Project structure (feature/domain-driven)

movingapi/
├── MovingApiApplication.java
│
├── auth/                          ← Login / Register
│   ├── AuthController, AuthService
│   └── dto/  LoginRequest, LoginResponse, RegisterRequest
│
├── common/                        ← Shared infrastructure
│   ├── exception/  BadRequestException, ResourceNotFoundException, GlobalExceptionHandler
│   └── response/  ApiResponse<T>
│
├── config/                        ← Spring config
│   ├── SecurityConfig
│   └── WebConfig  (CORS)
│
├── lookup/                        ← Seed / reference data
│   ├── region/  Region, RegionRepository
│   └── businessservice/  BusinessService, BusinessServiceRepository
│
├── user/                          ← users table
│   ├── User, UserRepository, UserService, UserController, UserRole
│   └── dto/  UserRequest, UserResponse
│
├── mover/                         ← movers + mover_regions + mover_services
│   ├── Mover, MoverRepository, MoverService, MoverController
│   └── dto/  MoverRequest, MoverResponse
│
├── vehicle/                       ← vehicles + vehicle_statuses
│   ├── Vehicle, VehicleStatus, VehicleRepository, VehicleStatusRepository
│   ├── VehicleService, VehicleController
│   └── dto/  VehicleRequest, VehicleResponse
│
├── rfq/                           ← rfqs + rfq_statuses
│   ├── Rfq, RfqStatus, RfqRepository, RfqStatusRepository
│   ├── RfqService, RfqController
│   └── dto/  RfqRequest, RfqResponse
│
├── bid/                           ← bids + bid_statuses
│   ├── Bid, BidStatus, BidRepository, BidStatusRepository
│   ├── BidService, BidController
│   └── dto/  BidRequest, BidResponse
│
├── move/                          ← moves + booking_statuses
│   ├── Move, MoveStatus, MoveRepository, MoveStatusRepository
│   ├── MoveService, MoveController
│   └── dto/  MoveRequest, MoveResponse
│
├── inventory/                     ← move_inventories + inventory_items
│   ├── Inventory, InventoryItem, InventoryRepository, InventoryItemRepository
│   ├── InventoryService, InventoryController
│   └── dto/  InventoryRequest, InventoryResponse
│
├── payment/                       ← payments (Stripe-backed, no lookup table)
│   ├── Payment, PaymentRepository
│   ├── PaymentService, PaymentController
│   └── dto/  PaymentRequest, PaymentResponse
