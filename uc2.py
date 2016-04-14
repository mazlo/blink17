import numpy as np
import matplotlib.pyplot as plt

N = 5

# UC2
avg_mysql = (49871,55739,59854,15217,129957)
avg_neo4j = (27413,27999,31267,42573,46820)
avg_stardog = (37962,38512,38396,36919,38238)
avg_virtuoso = (25701,26240,24961,26121,25573)

ind = np.arange(N)	# the x locations for the groups
width = 0.15		# the width of the bars
opacity = 0.4

fig, ax = plt.subplots()

rects1 = ax.bar(ind, avg_mysql, width,
                 alpha=opacity,
                 color='b',
                 label='mysql')

rects2 = ax.bar(ind + width, avg_neo4j, width,
                 alpha=opacity,
                 color='r',
                 label='neo4j')

rects3 = ax.bar(ind + (width*2), avg_stardog, width,
                 alpha=opacity,
                 color='g',
                 label='stardog')

rects4 = ax.bar(ind + (width*3), avg_virtuoso, width,
                 alpha=opacity,
                 color='y',
                 label='virtuoso')

plt.xlabel('Distributions')
plt.ylabel('ms')
plt.xticks(ind + width, ('50/50', '60/40', '70/30', '80/20', '90/10'))
plt.legend()

plt.tight_layout()
plt.show()

