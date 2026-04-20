<script setup lang="ts">
</script>

<template>
  <div class="dropdown">
    <slot name="drMain" />
    <div class="dropdown-content">
      <slot name="drContent" />
    </div>
  </div>
</template>

<!-- from https://www.w3schools.com/css/css_dropdowns.asp -->
<style>
.dropdown {
  position: relative;
}

/* Style the dropdown button */
.dropbtn {
  background-color: #4CAF50;
  color: white;
  padding: 16px;
  font-size: 16px;
  border: none;
  cursor: pointer;
}

/* Dropdown content */
.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 130px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  white-space: preserve nowrap;
}

/* Links inside dropdown content */
.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

/* Change color of dropdown links on hover */
.dropdown-content a:hover {
  background-color: #f1f1f1
}

/* Show the dropdown content on hover */
.dropdown:hover .dropdown-content {
  display: block;
}

/* Change background color of dropdown button on hover */
.dropdown:hover .dropbtn {
  background-color: #3e8e41;
}
</style>
