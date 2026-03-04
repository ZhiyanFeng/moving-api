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
├── booking/                       ← bookings + booking_statuses
│   ├── Booking, BookingStatus, BookingRepository, BookingStatusRepository
│   ├── BookingService, BookingController
│   └── dto/  BookingRequest, BookingResponse
│
├── inventory/                     ← inventories table
│   ├── Inventory, InventoryRepository, InventoryService, InventoryController
│   └── dto/  InventoryRequest, InventoryResponse
│
├── payment/                       ← payments + payment_statuses
│   ├── Payment, PaymentStatus, PaymentRepository, PaymentStatusRepository
│   ├── PaymentService, PaymentController
│   └── dto/  PaymentRequest, PaymentResponse
│
└── invoice/                       ← invoices + invoice_statuses
├── Invoice, InvoiceStatus, InvoiceRepository, InvoiceStatusRepository
├── InvoiceService, InvoiceController
└── dto/  InvoiceRequest, InvoiceResponse
