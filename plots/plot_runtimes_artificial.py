import numpy as np
import matplotlib.pyplot as plt

import pylab
params = {
	'font.family' : 'serif',
	'font.serif' : ['Times New Roman'],
	'font.size' : 15,
	'figure.figsize' : (8,13)
}
pylab.rcParams.update(params)

N = 39

colors = ['#6c82a3','#db8d8d','#c7c1c7','#897689']
ticklabels = ['vf4', 'vf3b', 'vf3', 'vf2b', 'vf2', 'vd7', 'vd6', 'vd5', 'vd3', 'vd2', 'vd1', 'sd3', 'sd2', 'sd1', 'qd6', 'qd5', 'qd4', 'qd3', 'qd2', 'qd1', 'dv1', 'duc7', 'duc6', 'duc5', 'duc4', 'duc3', 'duc2', 'duc1', 'dsv6', 'dsv5b', 'dsv5', 'dsv4', 'dsv3', 'dsv2', 'dsv1', 'dp4', 'dp3', 'dp2', 'dp1']
labels = ['MySQL', 'Neo4j', 'Stardog', 'Virtuoso']
markers = ['x','o','d','v']
hatches = ['','|||', '///','\\\\\\']

ind = np.arange(N)      # the x locations for the groups
width = 0.2            # the width of the bars
opacity = 0.8

fig, ax1 = plt.subplots()

mysql = [ 29.12, 4.88, 16.68, 837.76, 2673.32, 24.08, 63.04, 4.4, 2.08, 2.52, 0.88, 66.6, 148.28, 155.36, 11.72, 2.96, 1.64, 3.8, 1.48, 2.44, 0.64, 65.96, 366.36, 98.96, 112.32, 154.2, 262.16, 12.4, 383.96, 0, 86649.72, 932, 0.88, 96.76, 8.2, 12, 17.6, 9.8, 44.8 ]
neo4j = [ 17.16, 5.32, 24.8, 4.84, 631.96, 31.76, 49.68, 11.52, 8.48, 14.48, 6.68, 72.48, 52.68, 67.84, 18, 12.84, 20.84, 34.48, 11.6, 9.36, 5.56, 35.72, 963.48, 117.16, 93.52, 102.32, 540.64, 5.56, 13463.44, 4.48, 6.4, 7424.32, 5.68, 368.76, 472.04, 16.32, 44.24, 24.08, 126.76 ]
stardog = [ 2012.68, 1864.72, 4465.48, 2795.12, 4687.56, 925.8, 29.08, 680.96, 43.84, 733.76, 12.12, 15.76, 15.12, 15, 1021.4, 445.12, 956.24, 768.68, 663.72, 30.6, 6.52, 658.28, 205.44, 753.68, 213.28, 32.92, 592.32, 15.12, 662.68, 6330.16, 3547.64, 3445.48, 17.68, 59.4, 11.48, 22.56, 21.44, 61.68, 38.8 ]
virtuoso = [ 379.2, 1654.84, 1689, 76.6, 128.16, 88.48, 18.16, 58.84, 9.68, 15.96, 5.72, 4.4, 1.8, 7.96, 64.72, 31.76, 22.08, 17.88, 8.6, 10.4, 3.28, 23.2, 14.44, 295.2, 30.72, 10.56, 23.12, 16.72, 91.96, 15305.96, 3155.32, 1708.84, 7.48, 43.68, 191.52, 6.56, 7.72, 10.52, 24.88 ]

bars = [ mysql, neo4j, stardog, virtuoso ]

# plot lines
for i, bar in enumerate( bars ):
  additional_space = (width*i) + (width/2)
  ax1.barh( ind + additional_space, bar, width, color=colors[i], hatch=hatches[i], alpha=opacity, label=labels[i] )

#ax1.axvline( 1, color='black', alpha=0.3 )
#ax1.axvline( 10, color='black', alpha=0.3 )
#ax1.axvline( 100, color='black', alpha=0.3 )
#ax1.axvline( 1000, color='black', alpha=0.3 )
#ax1.axvline( 10000, color='black', alpha=0.3 )

ax1.set_xscale( 'symlog' )
ax1.set_xlabel( 'Query Runtime in Milliseconds (log-scaled)' )
ax1.set_xticklabels( [ 0, 1, 10, 100, 1000, 10000, 100000 ] )
#ax1.set_xticks( np.arange( 1, 5 ) )
#ax1.set_xlim([0,6])

ax1.set_ylabel( 'Query' )
ax1.set_yticklabels( ticklabels, fontsize=14 )
ax1.set_yticks( ind + 0.5 )
ax1.tick_params( axis='both', which='major', pad=5 )

# Shrink current axis's height by 10% on the bottom
box = ax1.get_position()
ax1.set_position([box.x0, box.y0, box.width, box.height + box.height * 0.02])

handles, labels = ax1.get_legend_handles_labels()
ax1.legend( handles[::-1], labels[::-1], loc='upper center', bbox_to_anchor=(0.5, 1.04), ncol=4, prop={'size':14}, frameon=False )

plt.tight_layout()
plt.show()
