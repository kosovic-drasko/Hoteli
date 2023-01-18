@GetMapping("/bookings/date/{startDate}/to/{endDate}")
public List<Booking> showBookingsByDate(
@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
logger.info("Inside showBookingsByDate() method of BookingController");
if ((startDate.compareTo(endDate)) > 0) {
return null;
} else {
return service.findAll().stream().filter(
(bookings) -> bookings.getCheckin().isAfter(startDate.minusDays(1)) && bookings.getCheckout().isBefore(endDate.plusDays(1)))
.collect(Collectors.toList());
}




getBookingsByDate(startDate:string,endDate:string): Observable<any> {
return this.http.get('http://localhost:8003/bookings/date/'+startDate+'/to/'+endDate);
}


