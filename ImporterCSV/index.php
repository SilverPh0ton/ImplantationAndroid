<?php
include_once 'core.php';
?>
<html>
 <head>
  <title>Importer CSV AGECTR</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
     <style>

         body {
             padding: 16px;
         }

         #books {
             font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
             border-collapse: collapse;
             width: 100%;
         }

         #books td, #books th {
             border: 1px solid #ddd;
             padding: 8px;
         }

         #books tr:nth-child(even){background-color: #f2f2f2;}

         #books tr:hover {background-color: #ddd;}

         #books th {
             padding-top: 12px;
             padding-bottom: 12px;
             text-align: left;
             background-color: #4CAF50;
             color: white;
         }

         .switch {
             position: relative;
             display: inline-block;
             width: 60px;
             height: 34px;
         }

         .switch input {
             opacity: 0;
             width: 0;
             height: 0;
         }

         .slider {
             position: absolute;
             cursor: pointer;
             top: 0;
             left: 0;
             right: 0;
             bottom: 0;
             background-color: #ccc;
             -webkit-transition: .4s;
             transition: .4s;
         }

         .slider:before {
             position: absolute;
             content: "";
             height: 26px;
             width: 26px;
             left: 4px;
             bottom: 4px;
             background-color: white;
             -webkit-transition: .4s;
             transition: .4s;
         }

         input:checked + .slider {
             background-color: #2196F3;
         }

         input:focus + .slider {
             box-shadow: 0 0 1px #2196F3;
         }

         input:checked + .slider:before {
             -webkit-transform: translateX(26px);
             -ms-transform: translateX(26px);
             transform: translateX(26px);
         }

         /* Rounded sliders */
         .slider.round {
             border-radius: 34px;
         }

         .slider.round:before {
             border-radius: 50%;
         }

         #importer {
             width: 50%;
             margin: 0 auto;
         }

         #apiTogglerTitle {
             display: inline-block;
         }

         #apiKey {
             display: block;
         }


     </style>
 </head>
 <body>
 <div id="importer">
     <h3 align="center">Import CSV</h3><br />
     <form method="post" enctype="multipart/form-data">
         <div align="center">
             <label>Sélectionner le fichier csv:</label>
             <input type="file" name="file" />
             <br />
             <input type="submit" name="submit" value="Import" class="btn btn-info" />
         </div>
     </form>
 </div>

 <div id="apiToggler">
     <h3 id="apiTogglerTitle">Api toggle</h3>
     <label class="switch" id="apiChecbox">
         <input type="checkbox">
         <span class="slider round"></span>
     </label>
     <input type="text" id="apiKey">
 </div>
 </body>
</html>