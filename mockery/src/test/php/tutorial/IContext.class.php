<?php
  interface IContext {
    function getData();
	  function hasPermission($permission);
	}
?>