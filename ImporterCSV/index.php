<html>
 <head>
  <title>AGECTR | XSLX Importer</title>
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

         #importer {
             width: 50%;
             margin: 0 auto;
         }


         #apiKeySection {
             display: block;
         }

         #optionLabel {
             margin-bottom: 10px;
         }


     </style>
 </head>
 <body>
 <div id="importer">
     <h3 align="center">Import XSLX</h3><br />
     <form method="post" enctype="multipart/form-data">
         <div align="center">
             <label>Sélectionner le fichier xslx:</label>
             <input type="file" name="file" />
             <br />
             <input id="option" type="checkbox" name="field" value="option">
             <label id="optionLabel" for="option">Import books from google books API</label>
             <div id="apiKeySection">
                 <p><b>*IMPORTANT :</b> Ajuster <b>« max_execution_time = 0 »</b> dans le <b>php.ini</b> pour un fichier excel volumineux.</p>
                 <label for="apiKey">API key</label>
                 <input type="input" id="apiKey" name="apiKey">
             </div>

             <br />
             <input type="submit" name="submit" value="Import" class="btn btn-info" />
         </div>
     </form>
 </div>


 <script src="js/index.js"></script>
 </body>
</html>

<?php
include_once 'core.php';
?>