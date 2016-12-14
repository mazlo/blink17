import numpy as np
import matplotlib.pyplot as plt

import pylab
params = {
          'font.family' : 'serif',
          'font.serif' : ['Times New Roman'],
          'font.size' : 15
}
pylab.rcParams.update(params)

N = 6

colors = ['#6c82a3','#db8d8d','#c7c1c7','#897689']
labels = ['MySQL', 'Neo4j', 'Stardog', 'Virtuoso']
markers = ['x','o','d','v']

# UC1
# 100/0, 90/10, 80/20, ..
avg_mysql1 = [570,16056,11181,10670,8304,7337]
avg_neo4j1 = [865,41530,31047,27504,26440,25316]
avg_stardog1 = [6065,30353,27604,27555,28184,28203]
avg_virtuoso1 = [371,9450,7485,6501,6968,6491]

# UC2
# 100/0, 90/10, 80/20, ..
avg_mysql2 = [1658,7960,7799,5677,6365,5702]
avg_neo4j2 = [405,67600,41694,27506,26031,26667]
avg_stardog2 = [14637,28160,28130,28113,28023,27743]
avg_virtuoso2 = [3682,7541,7416,6750,5979,6323]

# UC3
# 100/0, 90/10, 80/20, ..
avg_mysql3 = [1039,21539,16713,14336,8432,9514]
avg_neo4j3 = [18148,63425,29282,27849,25028,25028]
avg_stardog3 = [3463,28616,32598,29596,28971,28729]
avg_virtuoso3 = [1776,6020,7832,6879,7062,6156]

# UC4
# 100/0, 90/10, 80/20, ..
avg_mysql4 = [460,32457,19516,11253,9181,8330]
avg_neo4j4 = [767,54308,41159,31401,26514,27108]
avg_stardog4 = [2232,27462,31761,29356,29088,28592]
avg_virtuoso4 = [411,9249,8959,7940,7102,7010]

all_values1 = [ avg_mysql1, avg_neo4j1, avg_stardog1, avg_virtuoso1 ]
all_values2 = [ avg_mysql2, avg_neo4j2, avg_stardog2, avg_virtuoso2 ]
all_values3 = [ avg_mysql3, avg_neo4j3, avg_stardog3, avg_virtuoso3 ]
all_values4 = [ avg_mysql4, avg_neo4j4, avg_stardog4, avg_virtuoso4 ]

ind = np.arange(N)      # the x locations for the groups
width = 0.15            # the width of the bars
opacity = 0.4

fig, ( (ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, sharey=True)

bars = []

# plot lines
for i, y_values in enumerate( all_values1 ):
  additional_space = (width*2) + (width/2)
  x_values = [ x + additional_space for x,_ in enumerate( y_values ) ]
  ax1.plot( x_values, y_values, marker=markers[i], markersize=6, color=colors[i], label=labels[i], linewidth=1.5 )

# plot lines
for i, y_values in enumerate( all_values2 ):
  additional_space = (width*2) + (width/2);
  x_values = [ x + additional_space for x,_ in enumerate( y_values ) ]
  ax2.plot( x_values, y_values, marker=markers[i], markersize=6, color=colors[i], label=labels[i], linewidth=1.5 )

# plot lines
for i, y_values in enumerate( all_values3 ):
  additional_space = (width*2) + (width/2)
  x_values = [ x + additional_space for x,_ in enumerate( y_values ) ]
  ax3.plot( x_values, y_values, marker=markers[i], markersize=6, color=colors[i], label=labels[i], linewidth=1.5 )

# plot lines
for i, y_values in enumerate( all_values4 ):
  additional_space = (width*2) + (width/2);
  x_values = [ x + additional_space for x,_ in enumerate( y_values ) ]
  ax4.plot( x_values, y_values, marker=markers[i], markersize=6, color=colors[i], label=labels[i], linewidth=1.5 )

ax1.fill_between( ind + (width*2) + (width/2), avg_mysql1, avg_virtuoso1, alpha=opacity-0.3, hatch='//////', facecolor='#6c82a3' )
ax2.fill_between( ind + (width*2) + (width/2), avg_mysql2, avg_virtuoso2, alpha=opacity-0.3, hatch='//////', facecolor='#6c82a3' )
ax3.fill_between( ind + (width*2) + (width/2), avg_mysql3, avg_virtuoso3, alpha=opacity-0.3, hatch='//////', facecolor='#6c82a3' )
ax4.fill_between( ind + (width*2) + (width/2), avg_mysql4, avg_virtuoso4, alpha=opacity-0.3, hatch='//////', facecolor='#6c82a3' )

ax1.fill_between( ind + (width*2) + (width/2), avg_neo4j1, avg_stardog1, alpha=opacity-0.3, hatch='\\\\\\', facecolor='#db8d8d' )
ax2.fill_between( ind + (width*2) + (width/2), avg_neo4j2, avg_stardog2, alpha=opacity-0.3, hatch='\\\\\\', facecolor='#db8d8d' )
ax3.fill_between( ind + (width*2) + (width/2), avg_neo4j3, avg_stardog3, alpha=opacity-0.3, hatch='\\\\\\', facecolor='#db8d8d' )
ax4.fill_between( ind + (width*2) + (width/2), avg_neo4j4, avg_stardog4, alpha=opacity-0.3, hatch='\\\\\\', facecolor='#db8d8d' )

ax1.legend( loc='upper right', prop={'size':14} )
ax1.set_ylabel( 'ms' )

ax1.set_xlabel( 'Distributions in Use-Case 1 - Navigation' )
ax2.set_xlabel( 'Distributions in Use-Case 2 - Statistics' )
ax1.xaxis.set_label_position('top')
ax2.xaxis.set_label_position('top')

ax3.legend( loc='upper right', prop={'size':14} )
ax3.set_ylabel( 'ms' )

ax3.set_xlabel( 'Distributions in Use-Case 3 - Validation' )
ax4.set_xlabel( 'Distributions in Use-Case 4 - User Query' )
ax3.xaxis.set_label_position('top')
ax4.xaxis.set_label_position('top')

ax1.set_ylabel( 'ms' )

plt.setp( [ax1,ax2, ax3, ax4], xticks=ind + (width*2), xticklabels=['100/0','90/10','80/20','70/30','60/40','50/50'])

plt.rcParams['font.family'] = 'serif'
plt.tight_layout()
plt.show()