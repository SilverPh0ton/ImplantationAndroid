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
     </style>
 </head>
 <body>
  <h3 align="center">Import CSV</h3><br />
  <form method="post" enctype="multipart/form-data">
   <div align="center">
    <label>Sélectionner le fichier csv:</label>
    <input type="file" name="file" />
    <br />
    <input type="submit" name="submit" value="Import" class="btn btn-info" />
   </div>
  </form>
 </body>
</html>