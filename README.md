# TheaterDesign

**`TheaterDesign`** is a visualizer for theater layout. 
An XML description of the space allows for venue and show-specific elements to be displayed in a desktop
app and for drawings to be generated.

Venue elements can include walls, stage, proscenium arch, fixed lighting positions, etc.

Show-specific elements can include temporary lighting positions, sets, etc

Pipes and Luminaires can be specified as permanent installations in a venue or as transient setups for a show.

**`TheaterDesign`** is in the process of being reworked to include an interactive element so that XML
files do not have to be crafted by hand.
It is not yet there - right now, one would still need to write one's own XML.


The units of the numbers should not matter, so long as you are consistent. The drawings scale to fit the output medium.
I use inches, because I live in a backwards country that is out of step with the rest of the world.
The origin of the room is in the top left corner, but see `Proscenium`, below.

## Entities

Currently supported entities are:

### Venue

Names and describes the venue. There should be exactly one of these.

Attributes:
- `building` - string - name of building
- `room` - string - Name of room
- `width`,`depth`,`height` - dimensions of room

```dtd
<venue building="Federation HQ" room="Zefram Cochrane Auditorium" width="2160" depth="2160" height="1080" />
```  

### Wall

Describes the position of a wall. Not required, but your room will be devoid of context.
Walls are represented as a line between the two points specified.
Walls always use the top left corner of the drawing as their origin.

Attributes:
- `x1`,`y1` - coordinates of one end of the wall
- `x2`,`y2` - coordinates of the other end of the wall

```dtd
<wall x1="13" y1="27" x2="13" y2="743" />
```  

### Proscenium

Describes the location of a proscenium arch.
Optional.
If it is present, it redefines the origin for the room to be the downstage center of the proscenium.
Its own location is relative to the top left corner of the drawing.

Attributes:
- `x`,`y`,`z` - coordinates of the center of the proscenium arch at stage-floor level
- `height`,`width` - dimensions of the proscenium arch
- `depth` - thickness of the proscenium wall

```dtd
<proscenium x="1080" y="480" z="42" height="288" width="800" depth="14"/>
```

### Pipe

Typical lighting pipe.
Things get hung from it. 
By default, a fixed lighting position.

When a pipe is the child of a pipebase element, it is vertical and gets its coordinates from
its parent.
 
Attributes:
- `id` - name of pipe. Only needed when something will be referring to this pipe
- `x`,`y`,`z` - coordinates of the leftmost end of the pipe
- `length` - length of pipe

Children can be luminaire elements and these luminaires do not need to specify their pipe.

```dtd
<pipe id="1st electric" y="33" x="-288" z="184" length="576" />
```

### PipeBase

Weighted base with threaded receiver for 2" pipe.

Attributes:
- `x`,`y`,`z` - coordinates of the pipebase
- `id` - optional name of pipebase
- `owner` - optional owner of pipebase

Child should be a pipe element.

```dtd
<pipebase  y="33" x="-288" z="0"  />
```

### Event

Names the event for which the drawing is relevant.

Attributes:
- `id` - name of event

```dtd
<event id="Graduation"/>
```

### LuminaireDefinition

Describes a type of lighting instrument and provides an SVG icon for it. If you define a

Attributes:
- `name` - type of luminaire being described
- `width`, `length` - dimensions of the unit
- `weight` - weight of the unit
- `complete` - set if description is a complete luminaire

Child should be an svg element to be used as the icon for this type of luminaire.
Note that the WFL example builds on the svg drawing provided by the MFL example.

```dtd
<luminaire-definition name="Source Four PAR MFL" complete="1" width="10" length="11" weight="9.8" >
    <svg overflow="visible">
        <path fill="none" stroke="black" stroke-width="1"
              d="M -4.1 5 L -4.1 0.5 L -0.5 -5.7 L 0.5 -5.7 L 4.1 0.5 L 4.1 5 Z"
        />
    </svg>
</luminaire-definition>
<luminaire-definition name="Source Four PAR WFL" complete="1" width="10" length="11" weight="9.8" >
    <svg overflow="visible">
        <use xlink:href="#Source Four PAR MFL" />
        <path fill="none" stroke="black"
              d="M -5.1 5 L 0 1 L 5.1 5"
        />
    </svg>
</luminaire-definition>
```

### Luminaire

One instance of the referenced type of lighting instrument.

Attributes:
- `type` - type of luminaire being used
- `location` - position on pipe of this instrument
- `on` - name of pipe, required only if luminaire element is not a child of the pipe element it is hung on 
- `circuit`,`dimmer`,`address`,`channel`,`color` - descriptive information regarding this instrument
- `info`,`owner`,`label` - descriptive information regarding this instrument

[//]: # (- `target` - set if description is a complete luminaire)

[//]: # (- `rotation` - set if description is a complete luminaire)

```dtd
<luminaire type="Source Four PAR WFL" location="-202" dimmer="17" channel="36" color="R61" label="Center" />
```

### Setpiece

Encloses the component pieces of a setpiece and sets the location.

Attributes:
- `x`,`y` - coordinates of the setpiece
- 
```dtd
<setpiece x="0" y="186" >
  <set-platform x="-96" y="0" z="36">
    <shape rectangle="96 48" />
  </set-platform>
  <set-platform x="0" y="0" z="36">
    <shape rectangle="96 48" />
  </set-platform>
  <set-platform x="-96" y="-30" z="27">
    <shape rectangle="48 12" />
  </set-platform>
</setpiece>

```

### SetPlatform

Provides offset from the `Setpiece` origin at which the enclosed `Shape` is displayed.

Attributes:
- `x`,`y`,`z` - coordinates of the platform relative to the enclosing setpiece

Children should be shape elements

### Shape

Provides a shape with dimensions to describe a `SetPlatform`

Attributes:
- `rectangle` - describes dimension of a rectangle

### Drawing

Defines a drawing that will be produced. Currently, only the pipe detail is implemented.

Attributes:
- `id` - title of drawing
- `filename` - base name of HTML file to be generated
- `pipe` - name of pipe from which drawing detail is extracted

```dtd
<drawing id="First Electric Detail" filename="electric1" pipe="1st" />
```

## Example XML plot file:

```dtd
<plot>
  <drawing id="House Pipe Detail" filename="housepipe" pipe="house1" />
  <luminaire-definition name="Lighting Blaster 3000" complete="1" width="8" length="9" weight="14.5" >
    <svg overflow="visible">
      <path fill="white" stroke="black" stroke-width="1"
        d="M -4.5 5 L -4.5 -4.5 L -4 -4.5 A 3.5 3 0 0 1 4 -4.5 L 4.5 -4.5 L 4.5 5 L 5 6 L 5 6.5 L -5 6.5 L -5 6 Z"
      />
    </svg>
  </luminaire-definition>
  <venue building="Federation HQ" room="Zefram Cochrane Auditorium" width="2160" depth="2160" height="1080">
    <wall x1="13" y1="27" x2="13" y2="743" />
    <proscenium x="1080" y="480" z="42" height="288" width="800" depth="14"/>
    <zone id="DSC" x="0" y="-20" z="60" r="90" />
    <pipe id="house1" x="-520" y="-320" z="400" length="1040">
      <luminaire type="Lighting Blaster 3000" location="-202" address="1" channel="1" target="DSC" label="Center Left" />
      <luminaire type="Lighting Blaster 3000" location="202" address="101" channel="2" target="DSC" label="Center Right" />
    </pipe>
    <event id="Graduation">
      <luminaire type="Lighting Blaster 3000" on="house1" location="0" address="201" channel="3" target="DSC" label="Center" />
    </event>
  </venue>
</plot>
```

## UI

The UI shows a plan view, but Luminaire icons are not yet displayed.
I will need to figure out how to render SVG elements in Compose.

Currently, the app opens up the file named `$HOME/Theater/plotfile.xml`.
That is less than optimal, but enough for me to get by while I bring this codebase up to matching what the old code did.
Eventually this will need a file picker and perhaps a command-line argument to choose a file.

Buttons will turn on additional features.
The only one yet implemented displays the lighting instruments hung on each of the pipes.
Selecting a section highlights the appropriate pipe in the drawing.
I expect to build on this to allow the data to be edited.


