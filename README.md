
# Nulled Holograms

Uma livraria de holograms eficiente e de fácil uso.





## Funcionalidades

- Hologramas client-side (somente para alguns jogadores)
- Hologramas gerais server-side


## Uso/Exemplos

### Criando um novo holograma

```java
Holograms.newHologram(location, "Olá!", "Seja bem-vindo");
```

```java
List<String> lines = Lists.newArrayList();
lines.add("&aSeja bem-vindo.");

Holograms.newHologram(location, lines);
```

### Exibindo o holograma

```java
/*
exibindo somente para determinados jogadores
*/
hologram.displayTo(player1, player2, player3);
```

```java
/*
exibindo para todos os jogadores
*/
hologram.display();
```

