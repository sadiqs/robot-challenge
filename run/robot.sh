#!/usr/bin/env sh

curl http://localhost:8080/robot \
-H 'Content-Type: text/plain; charset=utf-8' \
--data-binary @- << EOF
PLACE 0,0,NORTH
MOVE
REPORT
EOF

#------------------
echo
echo

curl http://localhost:8080/robot \
-H 'Content-Type: text/plain; charset=utf-8' \
--data-binary @- << EOF
PLACE 0,0,NORTH
LEFT
REPORT
EOF

#------------------
echo
echo

curl http://localhost:8080/robot \
-H 'Content-Type: text/plain; charset=utf-8' \
--data-binary @- << EOF
PLACE 1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
EOF

echo