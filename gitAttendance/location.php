<?php
error_reporting(0);
require "init.php";
$username=$_POST["username"];
$longitude = $_POST["longitude"];
$latitude = $_POST["latitude"];
$date =$_POST["date"];


$sql = "INSERT INTO location(username, longitude, latitude, date) VALUES ('$username', '$longitude', '$latitude', '$date')";

?>