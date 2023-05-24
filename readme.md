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

## Funkcjonalności projektu
Aplikacja pozwala na
* Przeglądanie najbliższego repertuaru w kinie
* Wyświetlanie podstawowych informacji o prezentowanych 
* Wyświetlanie już zarezerwowanych miejsc da danego seansu
* Rezerwację miejsc w sali kinowej.

## Schemat bazy danych
![Schemat](https://raw.githubusercontent.com/flakboy/pbd2-projekt/main/db_scheme.png)

# Endpointy
* /shows
  * Metoda: GET
  * Służy do pobrania listy seansów w danym przedziale czasowym.
  * Do określania przedziału czasu służą parametry *startDate* oraz *endDate*
    
* /showReservations
  * Metoda: GET
  * Służy do pobrania aktualnie zarezerwowanych miejsc dla danego seansu
  * Do wskazania seansu dla którego chcemy pozyskać listę zarezerwowanych służy parametr *showId*

* /saveReservation
  * Metoda: POST
  * Przesyła do serwera informację o rezerwowanych miejscach
