	.text
	.globl main
main:
	movq $2, %rax
	movq $104, %rdi
	imulq %rdi, %rax
	ret
	.data
