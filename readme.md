# Projekt bazy danych
## Członkowie grupy
* Kacper Mazurczyk
* Mateusz Bywalec

## Temat projektu
Aplikacja webowa kina 

## Technologie
**Frontend:**
* HTML + CSS + JavaScript

**Backend:**
* Java (Hibernate)
* PostgreSQL

# Funkcjonalności projektu
Aplikacja pozwala na
* Przeglądanie najbliższego repertuaru w kinie
* Wyświetlanie podstawowych informacji o prezentowanych filmach
* Wyświetlanie już zarezerwowanych miejsc da danego seansu
* Dodawanie rezerwacji miejsc na seans.

# Schemat bazy danych
![Schemat](https://raw.githubusercontent.com/flakboy/pbd2-projekt/main/db_scheme_final.png)

# Endpointy
## /shows
* Metoda: GET
* Pobiera z bazy danych wszystkie seanse odbywające się w danym przedziale czasu
* Parametry:
  * *startDate* - data wyznaczająca początek przedziału czasu, w formacie tekstowym *YYYY-MM-DD*
  * *endDate* - data wyznaczająca koniec przedziału

### Generowany przez Hibernate kod SQL
```sql
select s1_0.showId, s1_0.date, s1_0.movieId, s1_0.movieRoomId, s1_0.time, s1_0.type
from Show s1_0 where s1_0.date>=? and s1_0.date<=?
```
    
## /get_show_data
  * Metoda: GET
  * Służy do pobrania informacji o seansie
   * Do wskazania seansu dla którego chcemy pozyskać listę zarezerwowanych służy parametr *showId*
  * Zwraca JSON zawierający:
    * Podstawowe informacje o seansie (id, przedstawiany film, typ seansu)
    * Informacje o przedstawianym filmie
    * Listę rezerwacji i zarezerwowanych miejsc

### Generowany przez Hibernate kod SQL
```sql
select s1_0.showId, s1_0.date,
       m1_0.movieId, m1_0.description, m1_0.duration, m1_0.title, 
       m2_0.movieRoomId, m2_0.rows, m2_0.seats, s1_0.time, s1_0.type
from Show s1_0
left join Movie m1_0 on m1_0.movieId=s1_0.movieId
left join MovieRoom m2_0 on m2_0.movieRoomId=s1_0.movieRoomId
where s1_0.showId=?

select r1_0.showId,r1_0.reservationId,r1_0.clientId
from Reservation r1_0 where r1_0.showId=?

select d1_0.reservationId, d1_0.reservationDetailId,d1_0.row, d1_0.seat
from ReservationDetail d1_0 where d1_0.reservationId=?
```

## /submit_reservation
 * Metoda: POST
 * Przesyła do serwera informacje z wypełnionego formularza, dotyczące rezerwowanych miejsc
  * showId - id seansu
  * userId - id użytkownika
    * na potrzeby testów, wartość jest zawsze ustawiona na 1
  * pola seat-*row*x*seat* - zaznaczone przez użytkownika pola, odpowiadające poszczególnym fotelom w sali kinowej

### Generowany przez Hibernate kod SQL
```sql
--pobranie informacji o show (w tym: sala kinowa, informacje o filmie)
select s1_0.showId, s1_0.date,
       m1_0.movieId, m1_0.description, m1_0.duration, m1_0.title,
       m2_0.movieRoomId, m2_0.rows, m2_0.seats,
       s1_0.time, s1_0.type
from Show s1_0
left join Movie m1_0 on m1_0.movieId=s1_0.movieId
left join MovieRoom m2_0 on m2_0.movieRoomId=s1_0.movieRoomId
where s1_0.showId=?

--sprawdzenie czy klient istnieje
select c1_0.clientId, c1_0.email,c1_0.firstName,c1_0.lastName,c1_0.phoneNumber from Client c1_0 where c1_0.clientId=?


insert into Reservation (clientId, showId) values (?, ?)

insert into ReservationDetail (reservationId, row, seat) values (?, ?, ?)
...
insert into ReservationDetail (reservationId, row, seat) values (?, ?, ?)

 
update ReservationDetail set reservationId=? where reservationDetailId=?
update ReservationDetail set reservationId=? where reservationDetailId=?
update ReservationDetail set reservationId=? where reservationDetailId=?
```
