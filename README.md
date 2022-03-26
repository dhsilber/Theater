# TheaterDesign

**`TheaterDesign`** is a visualizer for theater layout. 
An XML description of the space allows for venue and show-specific elements to be displayed in a desktop app and for SVG drawings to be generated.

Venue elements can include walls, stage, proscenium arch, fixed lighting positions, etc.

Show-specific elements can include temporary lighting positions, sets, etc

**`TheaterDesign`** is in the process of being reworked to include an interactive element so that XML files do not have to be crafted by hand.
It is not yet there - right now, one would still need to write one's own XML.

Example XML plot file:

```
<plot>
  <venue building="Federation HQ" room="Zefram Cochrane Auditorium" width="2160" depth="2160" height="1080">
    <wall />
    <proscenium x="1080" y="480" z="42" height="288" width="800" depth="14"/>
    <pipe id="house1" x="-520" y="-320" z="400" length="1040"/>
  </venue>
</plot>
```  

The units of the numbers should not matter, so long as you are consistant. The drawings scale to fit the output medium.
I use inches, because I live in a backwards country that is out of step with the rest of the world.
The origin of the room is in the top left corner, but see `Proscenium`, below.

## Entities

Currently supported entities are:

### Venue

Names and describes the venue. Required.

### Wall

Describes the position of a wall. Not required, but your room will be devoid of context.
Walls always use the top left corner of the drawing as their origin.

### Proscenium

Describes the location of a proscenium arch.
Optional.
If it is present, it redefines the origin for the room to be the downstage center of the proscenium.
It's own location is relative to the top left corner of the drawing.

### Pipe

Typical lighting pipe.
Things get hung from it.
