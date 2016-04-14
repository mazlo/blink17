import numpy as np
import matplotlib.pyplot as plt

N = 5

# UC1
avg_mysql = (81191,79318,57965,60557,14793)
avg_neo4j = (26826,26615,31364,31088,55472)
avg_stardog = (36232,38038,38615,39014,40812)
avg_virtuoso = (26115,25879,25887,28326,27988)

ind = np.arange(N)	# the x locations for the groups
width = 0.15		# the width of the bars
opacity = 0.4

fig, ax = plt.subplots()

rects1 = ax.bar(ind, avg_mysql, width,
                 alpha=opacity,
                 color='#F5A838',
                 label='mysql')

rects2 = ax.bar(ind + width, avg_neo4j, width,
                 alpha=opacity,
                 color='#77AB48',
                 label='neo4j')

rects3 = ax.bar(ind + (width*2), avg_stardog, width,
                 alpha=opacity,
                 color='#2A7596',
                 label='stardog')

rects4 = ax.bar(ind + (width*3), avg_virtuoso, width,
                 alpha=opacity,
                 color='#8EB9D7',
                 label='virtuoso')

plt.xlabel('Distributions')
plt.ylabel('ms')
plt.xticks(ind + width, ('50/50', '60/40', '70/30', '80/20', '90/10'))
plt.legend()

plt.tight_layout()
plt.show()

