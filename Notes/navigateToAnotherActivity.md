# Navigate to Another Activity

* Set up a button, its id, and link it to an button object
* Set up the onClickListener and implement the onClick() method as follows,

```java
// Implement the following inside onClick()
Intent intent = new Intent(<currentClassName>.this, <theTargetActivityClassName>.class);
startActivity(intent);

// For example, we want to go to sliding tiles from Game Centre
Intent intent = new Intent(GameCentre.this, StartingActivity.class);
startActivity(intent);

```

