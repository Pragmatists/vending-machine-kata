vending-machine-kata
====================

Przed Tobą zadanie vending-machine-kata &mdash; proste ćwiczenie, którego celem jest symulacja
 działania tytułowego&hellip; automatu sprzedającego 
 (ang. [vending machine]( https://en.wikipedia.org/wiki/Vending_machine ) ;-) )
 
Przygotowana jest już konfiguracja Mavena oraz dwie klasy: `VendingMachine`
 oraz `VendingMachineTest`. Ta druga zawiera jeden test, który możesz śmiało usunąć, gdyż nie jest
 on częścią rozwiązania. Możesz także zmienić nazwy przygotowanych klas czy też w ogóle je usunać.
 Poniżej umieszczone są wymagania, kluczowe obszary oraz, co najważniejsze, opis zadania.
 
Wymagania
---------

* Rozwiązanie napisz w języku Java. Preferowane wersje Javy to 7 lub 8.
* Do budowania projektu możesz użyć Maven lub Gradle. Jeśli chcesz skorzystać z innego narzędzia,
  dopisz w tym dokumencie, jakim poleceniem mamy uruchomić build z testami tak, aby nie wymagało to
  od nas specjalnej konfiguracji systemu ;-)
* Rozwiązanie przekaż nam jako pull request na GitHubie albo link do Twojego Git repozytorium,
  z które bez problemu sklonujemy kod.
 
Kluczowe obszary
----------------

* stosowanie Test-Driven Development
* podejście obiektowe i domenowe do problemu (Object-Oriented Programmin, Domain-Driven Design)
* czystość kodu produkcyjnego i testowego (Clean Code)
* poprawność algorytmiczna rozwiązania
* uzasadnione stosowanie wzorców projektowych (Design Patterns)
* atomowość i czytelność commitów
 
Opis zadania
------------

1. Maszyna zawiera produkty.
2. Produkty mogą być różnych typów (np. napój Cola 0.25l, batonik czekoladowy,
 woda mineralna 0.33l itd.).
3. Produkty są ułożone na półkach.
4. Na jednej półce może być ułożony tylko jeden typ produktu.
5. Każdy typ produktu ma określoną cenę.
6. Maszyna posiada wyświetlacz.
7. Jeżeli wybierzemy numer półki, na wyświetlaczu pojawia się cena produktu.
8. Produkty można kupić, wrzucając do automatu monety (nominały: 5, 2, 1, 0.5, 0.2, 0.1).
9. Po wrzuceniu każdej kolejnej monety na wyświetlaczu aktualizuje się kwota, którą należy
 jeszcze wrzucić.
10. Jeżeli wybierzemy numer półki i wrzucimy kwotę nie mniejszą, niż cena produktu, to
 otrzymujemy produkt oraz resztę (pod warunkiem, że automat ma z czego tę resztę wydać).
11. Jeżeli wybierzemy numer półki i wrzucimy kwotę mniejszą, niż cena produktu, to musimy
 wcisnąć “Anuluj”, aby otrzymać pieniądze z powrotem.
12. Jeżeli maszyna nie może wydać reszty, wyświetla komunikat informujący o tym oraz zwraca
 wrzucone monety, nie wydając produktu.
13. Przy wydawaniu reszty maszyna może korzystać tylko z monet, które posiada z poprzednich
 zakupów (nie drukujemy pieniędzy ;-) )


